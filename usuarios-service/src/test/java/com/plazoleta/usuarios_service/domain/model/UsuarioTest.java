package com.plazoleta.usuarios_service.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void settersYGetters_DeberianPersistirValores() {
        Usuario usuario = new Usuario();

        usuario.setId(1);
        usuario.setNombre("Sebastian");
        usuario.setApellido("Lopez");
        usuario.setCorreo("sebastian@mail.com");
        usuario.setPassword("hash");
        usuario.setRolId(2);
        usuario.setRol(RolNombre.PROPIETARIO);

        assertEquals(1, usuario.getId());
        assertEquals("Sebastian", usuario.getNombre());
        assertEquals("Lopez", usuario.getApellido());
        assertEquals("sebastian@mail.com", usuario.getCorreo());
        assertEquals("hash", usuario.getPassword());
        assertEquals(2, usuario.getRolId());
        assertEquals(RolNombre.PROPIETARIO, usuario.getRol());
    }
}
