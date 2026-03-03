# Proyecto Plazoleta

Sistema de microservicios para la gestión de usuarios, restaurantes, pedidos y notificaciones.  
Incluye servicios de usuarios, plazoleta, trazabilidad y notificaciones, con persistencia en MySQL y MongoDB, y mensajería con RabbitMQ.

**Servicios**
1. `usuarios-service` (HTTP 8080)  
   Base de datos: MySQL `usuarios_db` (puerto 3307).
2. `plazoleta-service` (HTTP 8081)  
   Base de datos: MySQL `plazoleta_db` (puerto 3308).
3. `trazabilidad_service` (HTTP 8082)  
   Base de datos: MongoDB `trazabilidad_db` (puerto 27017).
4. `notificaciones_service` (HTTP 8083)  
   Mensajería: RabbitMQ (puertos 5672 / 15672).

**Endpoints destacados**
1. `plazoleta-service`  
   `GET /pedido/reporte/eficiencia` (requiere JWT con rol `PROPIETARIO`).
2. `trazabilidad_service`  
   `GET /trazabilidad/pedido/{idPedido}`  
   `GET /trazabilidad/cliente/{idCliente}`  
   `GET /trazabilidad/restaurante/{idRestaurante}`

## Requisitos
1. Java 17.
2. Docker y Docker Compose.
3. PowerShell (para usar `dev.ps1` en Windows).

## Infraestructura (Docker)
El proyecto trae un `docker-compose.yml` en la raíz para levantar bases de datos y RabbitMQ.

```powershell
docker compose up -d
```

Servicios de infraestructura:
1. MySQL usuarios: `localhost:3307`
2. MySQL plazoleta: `localhost:3308`
3. MongoDB trazabilidad: `localhost:27017`
4. RabbitMQ: `localhost:5672` (AMQP) y `localhost:15672` (UI)

## Configuración de ambiente
1. JWT  
   Cada servicio usa el secreto `proyecto_pragma_plazoleta_firma_token_seguro_2026` (ver `application.properties`).
2. Notificaciones (Twilio)  
   Variables esperadas por `notificaciones_service`:
   - `TWILIO_ACCOUNT_SID`
   - `TWILIO_AUTH_TOKEN`
   - `TWILIO_PHONE_NUMBER`

El script `dev.ps1` define `NOTIFICACIONES_PROVIDER=console` si no está presente.

## Scripts SQL y datos iniciales
Los servicios MySQL inicializan datos con `data.sql`:
1. `usuarios-service/src/main/resources/data.sql`  
   Inserta roles y define la tabla `empleado_restaurante`.
2. `plazoleta-service/src/main/resources/data.sql`  
   Inserta categorías base.

MySQL se inicializa con `spring.sql.init.mode=always` y `spring.jpa.hibernate.ddl-auto=update` en cada servicio.

## Cómo iniciar los servicios
### Opción A: Script de desarrollo (recomendado)
El script `dev.ps1` levanta los 4 servicios y guarda logs en `run-logs/`.

```powershell
.\dev.ps1 start
```

Comandos útiles:
```powershell
.\dev.ps1 status
.\dev.ps1 stop
```

### Opción B: Manual por servicio
Ejecutar en cada carpeta de servicio:

```powershell
.\mvnw.cmd -Dmaven.test.skip=true spring-boot:run
```

Servicios:
1. `usuarios-service`
2. `plazoleta-service`
3. `trazabilidad_service`
4. `notificaciones_service`

## Puertos de cada servicio
1. `usuarios-service`: 8080
2. `plazoleta-service`: 8081
3. `trazabilidad_service`: 8082
4. `notificaciones_service`: 8083

## Notas
1. El `docker-compose.yml` solo levanta infraestructura, no los servicios Spring Boot.
2. Si un puerto está ocupado, `dev.ps1` permite liberar con `-ForceKillPorts`.
