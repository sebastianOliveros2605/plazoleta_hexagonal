package com.plazoleta.plazoleta_service.domain.constants;

public final class PedidoDomainConstants {

    public static final int LONGITUD_PIN_SEGURIDAD = 6;
    public static final int MINUTOS_EXPIRACION_PIN_SEGURIDAD = 5;
    public static final String MENSAJE_SMS_PEDIDO_LISTO = "Tu pedido esta listo. Tu PIN de seguridad es: %s";
    public static final String MENSAJE_PIN_OBLIGATORIO = "El PIN de seguridad es obligatorio para entregar el pedido.";
    public static final String MENSAJE_PIN_NO_COINCIDE = "El PIN de seguridad del pedido no coincide.";
    public static final String MENSAJE_PIN_EXPIRADO = "El PIN de seguridad del pedido ya expiro.";

    private PedidoDomainConstants() {
    }
}
