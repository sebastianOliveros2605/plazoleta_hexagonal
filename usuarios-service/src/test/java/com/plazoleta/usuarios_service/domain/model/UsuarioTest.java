package com.plazoleta.usuarios_service.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void assignAdminRole_DeberiaAsignarDatosCorrectos() {
        Usuario usuario = new Usuario();

        usuario.assignAdminRole();

        assertNotNull(usuario.getRol());
        assertEquals(1, usuario.getRol().getId());
        assertEquals(Rol.ADMIN, usuario.getRol().getNombre());
    }

    @Test
    void assignOwnerRole_DeberiaAsignarDatosCorrectos() {
        Usuario usuario = new Usuario();

        usuario.assignOwnerRole();

        assertNotNull(usuario.getRol());
        assertEquals(2, usuario.getRol().getId());
        assertEquals(Rol.PROPIETARIO, usuario.getRol().getNombre());
    }

    @Test
    void assignEmployeeRole_DeberiaAsignarDatosCorrectos() {
        Usuario usuario = new Usuario();

        usuario.assignEmployeeRole();

        assertNotNull(usuario.getRol());
        assertEquals(3, usuario.getRol().getId());
        assertEquals(Rol.EMPLEADO, usuario.getRol().getNombre());
    }

    @Test
    void assignClientRole_DeberiaAsignarDatosCorrectos() {
        Usuario usuario = new Usuario();

        usuario.assignClientRole();

        assertNotNull(usuario.getRol());
        assertEquals(4, usuario.getRol().getId());
        assertEquals(Rol.CLIENTE, usuario.getRol().getNombre());
    }
}

