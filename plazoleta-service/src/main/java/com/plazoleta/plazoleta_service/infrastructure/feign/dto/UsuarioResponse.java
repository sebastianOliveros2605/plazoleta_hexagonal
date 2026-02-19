package com.plazoleta.plazoleta_service.infrastructure.feign.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {
    private String nombre;
    private String apellido;
    private Long documentoIdentidad;
    private String celular;
    private Date fechaNacimiento;
    private String correo;
    private String password;
    private String rol;
}
