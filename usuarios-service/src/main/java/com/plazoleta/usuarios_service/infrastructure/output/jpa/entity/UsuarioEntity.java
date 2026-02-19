package com.plazoleta.usuarios_service.infrastructure.output.jpa.entity;

import java.util.Date;

import com.plazoleta.usuarios_service.domain.model.RolEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String apellido;

    @Column(nullable = false, unique = true)
    private Long documentoIdentidad;

    private String celular;

    private Date fechaNacimiento;

    @Column(nullable = false, unique = true)
    private String correo;

    private String password;

    @Enumerated(EnumType.STRING)
    private RolEnum rol;
}

