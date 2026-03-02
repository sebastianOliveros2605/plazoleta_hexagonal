package com.plazoleta.usuarios_service.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class RolTest {

    @Test
    void constructorYGetters_DeberianPersistirValores() {
        Rol rol = new Rol(10, "SUPERVISOR", "Rol de pruebas");

        assertEquals(10, rol.getId());
        assertEquals("SUPERVISOR", rol.getNombre());
        assertEquals("Rol de pruebas", rol.getDescripcion());
    }

    @Test
    void constantes_DeberianEstarDisponibles() {
        assertNotNull(Rol.ADMIN);
        assertNotNull(Rol.PROPIETARIO);
        assertNotNull(Rol.EMPLEADO);
        assertNotNull(Rol.CLIENTE);
    }
}

