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
    private Long idRestaurante;
    private Rol rol;

    public void assignEmployeeRole(){
        this.rol = new Rol(Rol.EMPLEADO_ID, Rol.EMPLEADO, null);
    }
    public void assignClientRole(){
        this.rol = new Rol(Rol.CLIENTE_ID, Rol.CLIENTE, null);
    }
    public void assignOwnerRole(){
        this.rol = new Rol(Rol.PROPIETARIO_ID, Rol.PROPIETARIO, null);
    }
    public void assignAdminRole(){
        this.rol = new Rol(Rol.ADMIN_ID, Rol.ADMIN, null);
    }

}
