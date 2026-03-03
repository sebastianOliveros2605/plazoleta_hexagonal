package com.plazoleta.usuarios_service.infrastructure.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UsuarioDTO {
    @Schema(example = "Sebastian")
    @NotBlank
    private String nombre;
    @Schema(example = "Gomez")
    @NotBlank
    private String apellido;
    @Schema(example = "1032456789")
    @NotNull
    @Positive
    private Long documentoIdentidad;
    @Schema(example = "+573001112233")
    @NotBlank
    @Size(max = 13)
    @Pattern(regexp = "^\\+?[0-9]+$")
    private String celular;
    @Schema(example = "1998-05-20")
    @NotNull
    private Date fechaNacimiento;
    @Schema(example = "sebastian@mail.com")
    @NotBlank
    @Email
    private String correo;
    @Schema(example = "ClaveSegura123*")
    @NotBlank
    private String password;

    @Schema(example = "CLIENTE")
    private String rol;
    @Schema(example = "10")
    private Long idRestaurante;
}
