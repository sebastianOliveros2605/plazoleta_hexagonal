package com.plazoleta.plazoleta_service.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.plazoleta.plazoleta_service.domain.exception.CampoObligatorioException;
import com.plazoleta.plazoleta_service.domain.exception.PrecioInvalidoException;
import org.junit.jupiter.api.Test;

class PlatoTest {

    @Test
    void constructorConArgumentos_DebeInicializarCamposYActivoEnTrue() {
        Plato plato = new Plato(
                "Ajiaco",
                "Ajiaco santafereno",
                20000,
                "https://img.test/ajiaco.png",
                1L,
                1L);

        assertEquals("Ajiaco", plato.getNombre());
        assertEquals("Ajiaco santafereno", plato.getDescripcion());
        assertEquals(20000, plato.getPrecio());
        assertEquals("https://img.test/ajiaco.png", plato.getUrlImagen());
        assertEquals(1L, plato.getIdCategoria());
        assertEquals(1L, plato.getIdRestaurante());
        assertTrue(plato.getActivo());
    }

    @Test
    void validarReglasDeCreacion_CuandoDatosValidos_DeberiaActivarPlato() {
        Plato plato = crearPlatoValido();
        plato.setActivo(null);

        plato.validarReglasDeCreacion();

        assertTrue(plato.getActivo());
    }

    @Test
    void validarReglasDeCreacion_CuandoNombreFalta_DebeLanzarExcepcion() {
        Plato plato = crearPlatoValido();
        plato.setNombre(" ");

        assertThrows(CampoObligatorioException.class, plato::validarReglasDeCreacion);
    }

    @Test
    void validarReglasDeCreacion_CuandoNombreEsNulo_DebeLanzarExcepcion() {
        Plato plato = crearPlatoValido();
        plato.setNombre(null);

        assertThrows(CampoObligatorioException.class, plato::validarReglasDeCreacion);
    }

    @Test
    void validarReglasDeCreacion_CuandoPrecioInvalido_DebeLanzarExcepcion() {
        Plato plato = crearPlatoValido();
        plato.setPrecio(0);

        assertThrows(PrecioInvalidoException.class, plato::validarReglasDeCreacion);
    }

    @Test
    void actualizarPrecioYDescripcion_CuandoDatosValidos_DebeActualizar() {
        Plato plato = crearPlatoValido();

        plato.actualizarPrecioYDescripcion("Descripcion actualizada", 25000);

        assertEquals("Descripcion actualizada", plato.getDescripcion());
        assertEquals(25000, plato.getPrecio());
    }

    @Test
    void actualizarPrecioYDescripcion_CuandoDescripcionFalta_DebeLanzarExcepcion() {
        Plato plato = crearPlatoValido();

        assertThrows(CampoObligatorioException.class, () -> plato.actualizarPrecioYDescripcion("", 10000));
    }

    @Test
    void validarReglasDeCreacion_CuandoCategoriaEsNula_DebeLanzarExcepcion() {
        Plato plato = crearPlatoValido();
        plato.setIdCategoria(null);

        assertThrows(CampoObligatorioException.class, plato::validarReglasDeCreacion);
    }

    @Test
    void validarReglasDeCreacion_CuandoRestauranteEsNulo_DebeLanzarExcepcion() {
        Plato plato = crearPlatoValido();
        plato.setIdRestaurante(null);

        assertThrows(CampoObligatorioException.class, plato::validarReglasDeCreacion);
    }

    @Test
    void validarReglasDeCreacion_CuandoUrlImagenFalta_DebeLanzarExcepcion() {
        Plato plato = crearPlatoValido();
        plato.setUrlImagen(" ");

        assertThrows(CampoObligatorioException.class, plato::validarReglasDeCreacion);
    }

    @Test
    void actualizarPrecioYDescripcion_CuandoPrecioNulo_DebeLanzarExcepcion() {
        Plato plato = crearPlatoValido();

        assertThrows(PrecioInvalidoException.class, () -> plato.actualizarPrecioYDescripcion("ok", null));
    }

    private Plato crearPlatoValido() {
        Plato plato = new Plato();
        plato.setNombre("Ajiaco");
        plato.setDescripcion("Ajiaco santafereno");
        plato.setPrecio(20000);
        plato.setUrlImagen("https://img.test/ajiaco.png");
        plato.setIdCategoria(1L);
        plato.setIdRestaurante(1L);
        return plato;
    }
}
