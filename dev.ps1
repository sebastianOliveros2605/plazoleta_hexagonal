param(
    [Parameter(Mandatory = $true)]
    [ValidateSet("start", "stop", "status")]
    [string]$Action,

    [switch]$ForceKillPorts
)

$ErrorActionPreference = "Stop"

$ROOT = Split-Path -Parent $MyInvocation.MyCommand.Path
$LOG_DIR = Join-Path $ROOT "run-logs"
$STATE_FILE = Join-Path $LOG_DIR "dev-state.json"

$USERS_NAME = "usuarios-service"
$USERS_DIR = Join-Path $ROOT $USERS_NAME
$USERS_PORT = 8080
$USERS_URL = "http://localhost:$USERS_PORT/"

$PLAZOLETA_NAME = "plazoleta-service"
$PLAZOLETA_DIR = Join-Path $ROOT $PLAZOLETA_NAME
$PLAZOLETA_PORT = 8081
$PLAZOLETA_URL = "http://localhost:$PLAZOLETA_PORT/"

$TRAZABILIDAD_NAME = "trazabilidad_service"
$TRAZABILIDAD_DIR = Join-Path $ROOT $TRAZABILIDAD_NAME
$TRAZABILIDAD_PORT = 8082
$TRAZABILIDAD_URL = "http://localhost:$TRAZABILIDAD_PORT/"

$NOTIFICACIONES_NAME = "notificaciones_service"
$NOTIFICACIONES_DIR = Join-Path $ROOT $NOTIFICACIONES_NAME
$NOTIFICACIONES_PORT = 8083
$NOTIFICACIONES_URL = "http://localhost:$NOTIFICACIONES_PORT/"

$STARTUP_TIMEOUT_SECONDS = 90
$POLL_INTERVAL_SECONDS = 2

function Ensure-Directories {
    if (-not (Test-Path $LOG_DIR)) {
        New-Item -ItemType Directory -Path $LOG_DIR | Out-Null
    }
}

function Get-ListeningPids([int]$Port) {
    $connections = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction SilentlyContinue
    if (-not $connections) {
        return @()
    }
    return $connections | Select-Object -ExpandProperty OwningProcess -Unique
}

function Get-PortOwnerSummary([int]$Port) {
    $pids = Get-ListeningPids $Port
    if (-not $pids -or $pids.Count -eq 0) {
        return "free"
    }

    $owners = foreach ($pidValue in $pids) {
        $proc = Get-Process -Id $pidValue -ErrorAction SilentlyContinue
        if ($proc) {
            "$($proc.ProcessName)($pidValue)"
        } else {
            "pid($pidValue)"
        }
    }
    return ($owners -join ", ")
}

function Ensure-PortIsAvailable([int]$Port, [string]$ServiceName) {
    $pids = Get-ListeningPids $Port
    if (-not $pids -or $pids.Count -eq 0) {
        return
    }

    if ($ForceKillPorts) {
        foreach ($pidValue in $pids) {
            cmd /c "taskkill /PID $pidValue /T /F" | Out-Null
        }
        Start-Sleep -Seconds 1
        $stillBusy = Get-ListeningPids $Port
        if ($stillBusy -and $stillBusy.Count -gt 0) {
            throw "No fue posible liberar el puerto $Port para $ServiceName. Aun ocupado por: $(Get-PortOwnerSummary $Port)"
        }
        return
    }

    throw "Puerto $Port ocupado para $ServiceName por: $(Get-PortOwnerSummary $Port). Usa -ForceKillPorts para liberar automaticamente."
}

function Start-ServiceProcess([string]$ServiceName, [string]$WorkDir) {
    if (-not (Test-Path $WorkDir)) {
        throw "No existe el directorio de ${ServiceName}: $WorkDir"
    }

    $stdoutPath = Join-Path $LOG_DIR "$ServiceName.log"
    $stderrPath = Join-Path $LOG_DIR "$ServiceName.err.log"

    $process = Start-Process -FilePath "cmd.exe" `
        -ArgumentList "/c", "mvnw.cmd -Dmaven.test.skip=true spring-boot:run" `
        -WorkingDirectory $WorkDir `
        -RedirectStandardOutput $stdoutPath `
        -RedirectStandardError $stderrPath `
        -PassThru

    return @{
        name = $ServiceName
        pid = $process.Id
        stdout = $stdoutPath
        stderr = $stderrPath
    }
}

function Wait-ForService([string]$ServiceName, [int]$Port, [string]$Url) {
    $deadline = (Get-Date).AddSeconds($STARTUP_TIMEOUT_SECONDS)
    while ((Get-Date) -lt $deadline) {
        $isListening = (Get-ListeningPids $Port).Count -gt 0
        if ($isListening) {
            try {
                $response = Invoke-WebRequest -Uri $Url -Method GET -UseBasicParsing -TimeoutSec 4
                if ($response.StatusCode -ge 200) {
                    return
                }
            } catch {
                if ($_.Exception.Response) {
                    return
                }
            }
        }
        Start-Sleep -Seconds $POLL_INTERVAL_SECONDS
    }

    throw "Timeout esperando a $ServiceName en puerto $Port."
}

function Write-State($StateObject) {
    $StateObject | ConvertTo-Json -Depth 5 | Set-Content -Path $STATE_FILE
}

function Read-State {
    if (-not (Test-Path $STATE_FILE)) {
        return $null
    }
    return Get-Content -Path $STATE_FILE -Raw | ConvertFrom-Json
}

function Stop-TrackedProcess([int]$TargetPid) {
    $proc = Get-Process -Id $TargetPid -ErrorAction SilentlyContinue
    if (-not $proc) {
        return $false
    }
    cmd /c "taskkill /PID $TargetPid /T /F" | Out-Null
    return $true
}

function Do-Start {

    if (-not $env:NOTIFICACIONES_PROVIDER) {
        $env:NOTIFICACIONES_PROVIDER = "console"

    }

    Ensure-Directories
    Ensure-PortIsAvailable $USERS_PORT $USERS_NAME
    Ensure-PortIsAvailable $PLAZOLETA_PORT $PLAZOLETA_NAME
    Ensure-PortIsAvailable $TRAZABILIDAD_PORT $TRAZABILIDAD_NAME
    Ensure-PortIsAvailable $NOTIFICACIONES_PORT $NOTIFICACIONES_NAME

    $usersProc = Start-ServiceProcess $USERS_NAME $USERS_DIR
    Wait-ForService $USERS_NAME $USERS_PORT $USERS_URL

    $plazoletaProc = Start-ServiceProcess $PLAZOLETA_NAME $PLAZOLETA_DIR
    Wait-ForService $PLAZOLETA_NAME $PLAZOLETA_PORT $PLAZOLETA_URL

    $trazabilidadProc = Start-ServiceProcess $TRAZABILIDAD_NAME $TRAZABILIDAD_DIR
    Wait-ForService $TRAZABILIDAD_NAME $TRAZABILIDAD_PORT $TRAZABILIDAD_URL

    $notificacionesProc = Start-ServiceProcess $NOTIFICACIONES_NAME $NOTIFICACIONES_DIR
    Wait-ForService $NOTIFICACIONES_NAME $NOTIFICACIONES_PORT $NOTIFICACIONES_URL

    $state = @{
        startedAt = (Get-Date).ToString("s")
        services = @($usersProc, $plazoletaProc, $trazabilidadProc, $notificacionesProc)
    }
    Write-State $state

    Write-Host "Servicios iniciados:"
    Write-Host "- $USERS_NAME PID=$($usersProc.pid) PORT=$USERS_PORT"
    Write-Host "- $PLAZOLETA_NAME PID=$($plazoletaProc.pid) PORT=$PLAZOLETA_PORT"
    Write-Host "- $TRAZABILIDAD_NAME PID=$($trazabilidadProc.pid) PORT=$TRAZABILIDAD_PORT"
    Write-Host "- $NOTIFICACIONES_NAME PID=$($notificacionesProc.pid) PORT=$NOTIFICACIONES_PORT"
    Write-Host "Logs en: $LOG_DIR"
}

function Do-Stop {
    Ensure-Directories
    $state = Read-State
    if (-not $state) {
        Write-Host "No hay estado previo. Intentando parada por puertos."
        foreach ($port in @($USERS_PORT, $PLAZOLETA_PORT, $TRAZABILIDAD_PORT, $NOTIFICACIONES_PORT)) {
            $pids = Get-ListeningPids $port
            foreach ($pidValue in $pids) {
                cmd /c "taskkill /PID $pidValue /T /F" | Out-Null
                Write-Host "Proceso detenido por puerto ${port}: PID=$pidValue"
            }
        }
        return
    }

    foreach ($svc in $state.services) {
        $stopped = Stop-TrackedProcess ([int]$svc.pid)
        if ($stopped) {
            Write-Host "$($svc.name) detenido. PID=$($svc.pid)"
        } else {
            Write-Host "$($svc.name) no estaba corriendo. PID=$($svc.pid)"
        }
    }

    if (Test-Path $STATE_FILE) {
        Remove-Item -Path $STATE_FILE -Force
    }
}

function Do-Status {
    Ensure-Directories
    $state = Read-State

    Write-Host "Puertos:"
    Write-Host "- 8080 -> $(Get-PortOwnerSummary $USERS_PORT)"
    Write-Host "- 8081 -> $(Get-PortOwnerSummary $PLAZOLETA_PORT)"
    Write-Host "- 8082 -> $(Get-PortOwnerSummary $TRAZABILIDAD_PORT)"
    Write-Host "- 8083 -> $(Get-PortOwnerSummary $NOTIFICACIONES_PORT)"

    if ($state) {
        Write-Host "Estado guardado: $($state.startedAt)"
        foreach ($svc in $state.services) {
            $running = [bool](Get-Process -Id ([int]$svc.pid) -ErrorAction SilentlyContinue)
            Write-Host "- $($svc.name): PID=$($svc.pid) RUNNING=$running"
        }
    } else {
        Write-Host "Sin estado guardado en $STATE_FILE"
    }
}

switch ($Action) {
    "start" { Do-Start; break }
    "stop" { Do-Stop; break }
    "status" { Do-Status; break }
}
