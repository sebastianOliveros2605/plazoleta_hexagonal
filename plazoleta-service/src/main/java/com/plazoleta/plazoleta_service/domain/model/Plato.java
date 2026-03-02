package com.plazoleta.plazoleta_service.domain.model;

import com.plazoleta.plazoleta_service.domain.exception.CampoObligatorioException;
import com.plazoleta.plazoleta_service.domain.exception.PrecioInvalidoException;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Plato {
    
    private Long id;
    private String nombre; 
    private String descripcion;
    private Integer precio;
    private String urlImagen;
    private Long idCategoria;
    private Boolean activo;
    private Long idRestaurante;

    public Plato(String nombre,String descripcion, Integer precio, String urlImagen,Long idCategoria, Long idRestaurante){
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.urlImagen = urlImagen;
        this.idCategoria = idCategoria;
        this.idRestaurante = idRestaurante;
        this.activo = true;
    }
    public Plato() {}

    public void validarReglasDeCreacion() {
        validarCampoObligatorio(nombre, "nombre");
        validarCampoObligatorio(descripcion, "descripcion");
        validarCampoObligatorio(urlImagen, "urlImagen");
        if (idCategoria == null) {
            throw new CampoObligatorioException("categoria");
        }
        if (idRestaurante == null) {
            throw new CampoObligatorioException("idRestaurante");
        }
        validarPrecio(precio);
        activo = true;
    }

    public void actualizarPrecioYDescripcion(String nuevaDescripcion, Integer nuevoPrecio) {
        validarCampoObligatorio(nuevaDescripcion, "descripcion");
        validarPrecio(nuevoPrecio);
        descripcion = nuevaDescripcion;
        precio = nuevoPrecio;
    }

    private void validarPrecio(Integer valorPrecio) {
        if (valorPrecio == null || valorPrecio <= 0) {
            throw new PrecioInvalidoException();
        }
    }

    private void validarCampoObligatorio(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new CampoObligatorioException(campo);
        }
    }

    public void habilitarDeshabilitarPlato(Boolean habilitar) {
        this.activo = habilitar;
    }
}
