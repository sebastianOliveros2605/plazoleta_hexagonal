package com.plazoleta.notificaciones_service.application;

import com.plazoleta.notificaciones_service.domain.constants.NotificacionDomainConstants;
import com.plazoleta.notificaciones_service.domain.model.Notificacion;
import com.plazoleta.notificaciones_service.domain.puertos.out.IMensajeriaPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EnviarNotificacionServiceTest {

    @Mock
    private IMensajeriaPort mensajeriaPort;

    @InjectMocks
    private EnviarNotificacionService enviarNotificacionService;

    @Test
    void enviarSms_CuandoNotificacionEsValida_DeberiaDelegarEnvio() {
        Notificacion notificacion = new Notificacion();
        notificacion.setCelularDestino("+573001112233");
        notificacion.setPin("123456");
        notificacion.setMensaje("Su pedido esta listo");

        enviarNotificacionService.enviarSms(notificacion);

        verify(mensajeriaPort).enviarSms(notificacion);
    }

    @Test
    void enviarSms_CuandoCelularDestinoEsNulo_DeberiaLanzarExcepcion() {
        Notificacion notificacion = new Notificacion();
        notificacion.setCelularDestino(null);
        notificacion.setPin("123456");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> enviarNotificacionService.enviarSms(notificacion)
        );

        assertEquals(NotificacionDomainConstants.MENSAJE_NOTIFICACION_INVALIDA, exception.getMessage());
        verify(mensajeriaPort, never()).enviarSms(notificacion);
    }

    @Test
    void enviarSms_CuandoCelularDestinoEsSoloEspacios_DeberiaLanzarExcepcion() {
        Notificacion notificacion = new Notificacion();
        notificacion.setCelularDestino("   ");
        notificacion.setPin("123456");

        assertThrows(IllegalArgumentException.class, () -> enviarNotificacionService.enviarSms(notificacion));
        verify(mensajeriaPort, never()).enviarSms(notificacion);
    }

    @Test
    void enviarSms_CuandoPinEsVacio_DeberiaLanzarExcepcion() {
        Notificacion notificacion = new Notificacion();
        notificacion.setCelularDestino("+573001112233");
        notificacion.setPin("");

        assertThrows(IllegalArgumentException.class, () -> enviarNotificacionService.enviarSms(notificacion));
        verify(mensajeriaPort, never()).enviarSms(notificacion);
    }
}
