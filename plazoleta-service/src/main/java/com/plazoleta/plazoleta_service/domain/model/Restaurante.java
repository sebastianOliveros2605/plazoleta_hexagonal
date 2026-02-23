package com.plazoleta.plazoleta_service.domain.model;

import static com.plazoleta.plazoleta_service.domain.constants.DomainConstants.LONGITUD_MAXIMA_TELEFONO;
import static com.plazoleta.plazoleta_service.domain.constants.DomainConstants.REGEX_NUMERICO;
import static com.plazoleta.plazoleta_service.domain.constants.DomainConstants.REGEX_TELEFONO;

import com.plazoleta.plazoleta_service.domain.exception.CampoObligatorioException;
import com.plazoleta.plazoleta_service.domain.exception.NitInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.NombreInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.TelefonoInvalidoException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Restaurante {
    private Long id;
    private String nombre;
    private String nit;
    private String direccion;
    private String telefono;
    private String urlLogo;
    private Integer idPropietario;

    public Restaurante(String nombre, String nit, String direccion,
            String telefono, String urlLogo, Integer idPropietario) {

        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
        this.telefono = telefono;
        this.urlLogo = urlLogo;
        this.idPropietario = idPropietario;
    }

    public Restaurante(){}

    public void validarReglasDeNegocio() {
        validarNombre(nombre);
        validarNit(nit);
        validarTelefono(telefono);
        validarCampoObligatorio(direccion, "direccion");
        validarCampoObligatorio(urlLogo, "urlLogo");
        if (idPropietario == null) {
            throw new CampoObligatorioException("idPropietario");
        }
    }

    private void validarNombre(String valorNombre) {
        validarCampoObligatorio(valorNombre, "nombre");
        if (valorNombre.trim().matches(REGEX_NUMERICO)) {
            throw new NombreInvalidoException();
        }
    }

    private void validarNit(String valorNit) {
        validarCampoObligatorio(valorNit, "nit");
        if (!valorNit.matches(REGEX_NUMERICO)) {
            throw new NitInvalidoException();
        }
    }

    private void validarTelefono(String valorTelefono) {
        validarCampoObligatorio(valorTelefono, "telefono");
        if (valorTelefono.length() > LONGITUD_MAXIMA_TELEFONO) {
            throw new TelefonoInvalidoException();
        }
        if (!valorTelefono.matches(REGEX_TELEFONO)) {
            throw new TelefonoInvalidoException();
        }
    }

    private void validarCampoObligatorio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new CampoObligatorioException(campo);
        }
    }
}
