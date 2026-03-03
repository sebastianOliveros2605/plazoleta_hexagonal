package com.plazoleta.usuarios_service.domain.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Usuario {

    private Integer id;
    private String nombre;
    private String apellido;
    private Long documentoIdentidad;
    private String celular;
    private Date fechaNacimiento;
    private String correo;
    private String password;
    private Integer rolId;
    private RolNombre rol;

}
