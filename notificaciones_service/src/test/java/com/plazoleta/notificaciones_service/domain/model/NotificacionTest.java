package com.plazoleta.notificaciones_service.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NotificacionTest {

    @Test
    void gettersYSetters_DeberianMantenerValoresAsignados() {
        Notificacion notificacion = new Notificacion();

        notificacion.setCelularDestino("+573001112233");
        notificacion.setPin("987654");
        notificacion.setMensaje("Codigo de verificacion");

        assertEquals("+573001112233", notificacion.getCelularDestino());
        assertEquals("987654", notificacion.getPin());
        assertEquals("Codigo de verificacion", notificacion.getMensaje());
    }

    @Test
    void gettersYSetters_CuandoValoresNulos_DeberianPermitirse() {
        Notificacion notificacion = new Notificacion();

        notificacion.setCelularDestino(null);
        notificacion.setPin(null);
        notificacion.setMensaje(null);

        assertNull(notificacion.getCelularDestino());
        assertNull(notificacion.getPin());
        assertNull(notificacion.getMensaje());
    }
}
