package com.plazoleta.usuarios_service.infrastructure.input.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import com.plazoleta.usuarios_service.application.dto.LoginCommand;
import com.plazoleta.usuarios_service.application.dto.LoginResult;
import com.plazoleta.usuarios_service.application.useCase.ConsultarUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.CrearUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.LoginUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.LoginRequest;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.LoginResponse;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.UsuarioDTO;
import com.plazoleta.usuarios_service.infrastructure.input.rest.mapper.UsuarioDTOMapper;
import com.plazoleta.usuarios_service.infrastructure.security.JwtService;
import com.plazoleta.usuarios_service.infrastructure.security.SecurityConstants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Operaciones del microservicio de usuarios")
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final LoginUsuarioUseCase loginUsuarioUseCase;
    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;
    private final UsuarioDTOMapper usuarioDTOMapper;
    private final JwtService jwtService;

    @Operation(
            summary = "Crear propietario",
            description = "Crea un usuario con rol PROPIETARIO. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_ADMIN)
    @PostMapping("/propietario")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearPropietario(
            @Valid @RequestBody UsuarioDTO request) {

        Usuario usuario = usuarioDTOMapper.toUsuarioDomain(request);
        crearUsuarioUseCase.crearPropietario(usuario);
    }

    @Operation(
            summary = "Crear empleado",
            description = "Crea un usuario con rol EMPLEADO asociado al propietario autenticado.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_PROPIETARIO)
    @PostMapping("/empleado")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearEmpleado(
            @Valid @RequestBody UsuarioDTO request,
            Authentication authentication) {

        Usuario usuario = usuarioDTOMapper.toUsuarioDomain(request);
        Integer idPropietarioAutenticado = Integer.parseInt(authentication.getName());
        crearUsuarioUseCase.crearEmpleado(usuario, idPropietarioAutenticado);
    }

    @Operation(summary = "Crear cliente", description = "Crea un usuario con rol CLIENTE.")
    @PostMapping("/cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearCliente(
            @Valid @RequestBody UsuarioDTO request) {

        Usuario usuario = usuarioDTOMapper.toUsuarioDomain(request);
        crearUsuarioUseCase.crearCliente(usuario);
    }

    @Operation(
            summary = "Crear administrador",
            description = "Crea un usuario con rol ADMIN. Requiere rol ADMIN.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize(SecurityConstants.TIENE_ROL_ADMIN)
    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearAdmin(
            @Valid @RequestBody UsuarioDTO request) {

        Usuario usuario = usuarioDTOMapper.toUsuarioDomain(request);
        crearUsuarioUseCase.crearAdmin(usuario);
    }

    @Operation(summary = "Login", description = "Autentica un usuario y retorna el token JWT.")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResult authResult = loginUsuarioUseCase.login(new LoginCommand(request.correo(), request.password()));
        String token = jwtService.generateToken(authResult.idUsuario(), authResult.correo(), authResult.rol());
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(
            summary = "Consultar usuario por id",
            description = "Retorna la informacion del usuario por su identificador.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> consultarUsuario(@PathVariable Integer idUsuario) {
        Usuario usuario = consultarUsuarioUseCase.consultarPorId(idUsuario);
        return ResponseEntity.ok(usuarioDTOMapper.toDTO(usuario));
    }

    @Operation(
            summary = "Consultar rol de usuario",
            description = "Retorna el nombre del rol de un usuario por id.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("consultarRol/{idUsuario}")
    public ResponseEntity<String> consultarRolUsuario(@PathVariable Integer idUsuario) {
        Usuario usuario = consultarUsuarioUseCase.consultarPorId(idUsuario);
        return ResponseEntity.ok(usuario.getRol().name());
    }

    @Operation(
            summary = "Validar existencia de usuario",
            description = "Retorna true si el usuario existe por id, false en caso contrario.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("existeUsuario/{idUsuario}")
    public ResponseEntity<Boolean> existeUsuario(@PathVariable Integer idUsuario) {
        boolean existe = consultarUsuarioUseCase.existePorId(idUsuario);
        return ResponseEntity.ok(existe);
    }

}
