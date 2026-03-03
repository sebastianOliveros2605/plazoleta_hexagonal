package com.plazoleta.usuarios_service.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class RolTest {

    @Test
    void rolNombreEnum_DeberiaExponerValoresEsperados() {
        assertEquals("ADMIN", RolNombre.ADMIN.name());
        assertEquals("PROPIETARIO", RolNombre.PROPIETARIO.name());
        assertEquals("EMPLEADO", RolNombre.EMPLEADO.name());
        assertEquals("CLIENTE", RolNombre.CLIENTE.name());
    }
}
