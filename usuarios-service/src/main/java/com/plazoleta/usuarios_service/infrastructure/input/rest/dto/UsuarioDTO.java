package com.plazoleta.usuarios_service.infrastructure.input.rest.dto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioDTO {
    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @NotNull
    @Positive
    private Long documentoIdentidad;
    @NotBlank
    @Size(max = 13)
    @Pattern(regexp = "^\\+?[0-9]+$")
    private String celular;
    @NotNull
    private Date fechaNacimiento;
    @NotBlank
    @Email
    private String correo;
    @NotBlank
    private String password;

    private String rol;
}
