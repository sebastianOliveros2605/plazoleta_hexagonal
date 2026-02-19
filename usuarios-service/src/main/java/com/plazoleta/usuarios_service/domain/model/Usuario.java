package com.plazoleta.usuarios_service.domain.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

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
    private RolEnum rol;
    
}
