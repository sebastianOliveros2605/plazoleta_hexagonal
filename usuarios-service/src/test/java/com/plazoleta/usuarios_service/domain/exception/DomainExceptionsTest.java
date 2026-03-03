package com.plazoleta.usuarios_service.domain.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DomainExceptionsTest {

    @Test
    void credencialesInvalidasException_DeberiaTenerMensajeEsperado() {
        assertEquals("Credenciales invalidas", new CredencialesInvalidasException().getMessage());
    }

    @Test
    void datosInvalidosException_DeberiaConservarMensaje() {
        assertEquals("dato invalido", new DatosInvalidosException("dato invalido").getMessage());
    }

    @Test
    void emailDuplicadoException_DeberiaTenerMensajeEsperado() {
        assertEquals("El correo ya esta registrado", new EmailDuplicadoException().getMessage());
    }

    @Test
    void menorDeEdadException_DeberiaTenerMensajeEsperado() {
        assertEquals("El usuario debe ser mayor de edad", new MenorDeEdadException().getMessage());
    }

    @Test
    void rolNoEncontradoException_DeberiaIncluirNombre() {
        assertEquals("Rol no encontrado para nombre: ADMIN", new RolNoEncontradoException("ADMIN").getMessage());
    }

    @Test
    void usuarioNoEncontradoException_DeberiaTenerMensajeEsperado() {
        assertEquals("Usuario no encontrado", new UsuarioNoEncontradoException().getMessage());
    }
}
