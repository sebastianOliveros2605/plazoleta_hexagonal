# Plazoleta Service

## HU18 - Eficiencia de pedidos

Endpoint expuesto para propietarios:

- `GET /pedido/reporte/eficiencia`

Requiere token JWT con rol `PROPIETARIO`.

Parametros opcionales:

- `idPedido`
- `idEmpleado`
- `fechaDesde` (ISO-8601, ejemplo `2026-02-01T00:00:00`)
- `fechaHasta` (ISO-8601, ejemplo `2026-02-28T23:59:59`)
- `incluirDetalleTransiciones` (`true|false`, por defecto `true`)

Notas:

- `plazoleta-service` calcula el reporte.
- `trazabilidad_service` solo provee el historial por restaurante.
