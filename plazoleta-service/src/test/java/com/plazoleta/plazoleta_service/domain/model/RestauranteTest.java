package com.plazoleta.plazoleta_service.domain.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.plazoleta.plazoleta_service.domain.exception.CampoObligatorioException;
import com.plazoleta.plazoleta_service.domain.exception.NitInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.NombreInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.TelefonoInvalidoException;
import org.junit.jupiter.api.Test;

class RestauranteTest {

    @Test
    void constructorConArgumentos_DebeInicializarCampos() {
        Restaurante restaurante = new Restaurante(
                "Sazon Costena",
                "900123456",
                "Calle 10 # 5 - 20",
                "+573001234567",
                "https://img.test/logo.png",
                7);

        assertEquals("Sazon Costena", restaurante.getNombre());
        assertEquals("900123456", restaurante.getNit());
        assertEquals("Calle 10 # 5 - 20", restaurante.getDireccion());
        assertEquals("+573001234567", restaurante.getTelefono());
        assertEquals("https://img.test/logo.png", restaurante.getUrlLogo());
        assertEquals(7, restaurante.getIdPropietario());
    }

    @Test
    void validarReglasDeNegocio_CuandoDatosValidos_NoDebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();

        assertDoesNotThrow(restaurante::validarReglasDeNegocio);
    }

    @Test
    void validarReglasDeNegocio_CuandoNombreSoloNumeros_DebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setNombre("1234");

        assertThrows(NombreInvalidoException.class, restaurante::validarReglasDeNegocio);
    }

    @Test
    void validarReglasDeNegocio_CuandoNombreEsNulo_DebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setNombre(null);

        assertThrows(CampoObligatorioException.class, restaurante::validarReglasDeNegocio);
    }

    @Test
    void validarReglasDeNegocio_CuandoNitNoNumerico_DebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setNit("ABC123");

        assertThrows(NitInvalidoException.class, restaurante::validarReglasDeNegocio);
    }

    @Test
    void validarReglasDeNegocio_CuandoTelefonoNoCumpleFormato_DebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setTelefono("+57A300");

        assertThrows(TelefonoInvalidoException.class, restaurante::validarReglasDeNegocio);
    }

    @Test
    void validarReglasDeNegocio_CuandoDireccionFalta_DebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setDireccion(" ");

        assertThrows(CampoObligatorioException.class, restaurante::validarReglasDeNegocio);
    }

    @Test
    void validarReglasDeNegocio_CuandoTelefonoEsDemasiadoLargo_DebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setTelefono("+57300123456789");

        assertThrows(TelefonoInvalidoException.class, restaurante::validarReglasDeNegocio);
    }

    @Test
    void validarReglasDeNegocio_CuandoUrlLogoFalta_DebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setUrlLogo(" ");

        assertThrows(CampoObligatorioException.class, restaurante::validarReglasDeNegocio);
    }

    @Test
    void validarReglasDeNegocio_CuandoIdPropietarioEsNulo_DebeLanzarExcepcion() {
        Restaurante restaurante = crearRestauranteValido();
        restaurante.setIdPropietario(null);

        assertThrows(CampoObligatorioException.class, restaurante::validarReglasDeNegocio);
    }

    private Restaurante crearRestauranteValido() {
        Restaurante restaurante = new Restaurante();
        restaurante.setNombre("Sazon Costena");
        restaurante.setNit("900123456");
        restaurante.setDireccion("Calle 10 # 5 - 20");
        restaurante.setTelefono("+573001234567");
        restaurante.setUrlLogo("https://img.test/logo.png");
        restaurante.setIdPropietario(7);
        return restaurante;
    }
}
