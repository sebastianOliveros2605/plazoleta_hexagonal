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

import com.plazoleta.usuarios_service.application.useCase.ConsultarUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.CrearUsuarioUseCase;
import com.plazoleta.usuarios_service.application.useCase.LoginUsuarioUseCase;
import com.plazoleta.usuarios_service.domain.model.Usuario;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.LoginRequest;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.LoginResponse;
import com.plazoleta.usuarios_service.infrastructure.input.rest.dto.UsuarioDTO;
import com.plazoleta.usuarios_service.infrastructure.input.rest.mapper.UsuarioDTOMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final CrearUsuarioUseCase crearUsuarioUseCase;
    private final LoginUsuarioUseCase loginUsuarioUseCase;
    private final ConsultarUsuarioUseCase consultarUsuarioUseCase;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/propietario")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearPropietario(
            @Valid @RequestBody UsuarioDTO request) {

        Usuario usuario = UsuarioDTOMapper.toUsuarioDomain(request, 2);
        crearUsuarioUseCase.crearUsuario(usuario);
    }

    @PreAuthorize("hasRole('PROPIETARIO')")
    @PostMapping("/empleado")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearEmpleado(
            @Valid @RequestBody UsuarioDTO request) {

        Usuario usuario = UsuarioDTOMapper.toUsuarioDomain(request, 3);
        crearUsuarioUseCase.crearUsuario(usuario);
    }

    @PostMapping("/cliente")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearCliente(
            @Valid @RequestBody UsuarioDTO request) {

        Usuario usuario = UsuarioDTOMapper.toUsuarioDomain(request, 4);
        crearUsuarioUseCase.crearUsuario(usuario);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public void crearAdmin(
            @Valid @RequestBody UsuarioDTO request) {

        Usuario usuario = UsuarioDTOMapper.toUsuarioDomain(request, 1);
        crearUsuarioUseCase.crearUsuario(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                loginUsuarioUseCase.login(request));
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioDTO> consultarUsuario(@PathVariable Integer idUsuario) {
        System.out.println(">>>>>>>>>>>>> ENTRA A CONSULTAR SI EXISTE ID : "+idUsuario);
        Usuario usuario = consultarUsuarioUseCase.consultarPorId(idUsuario);
        return ResponseEntity.ok(UsuarioDTOMapper.toDTO(usuario));
    }

    @GetMapping("consultarRol/{idUsuario}")
    public ResponseEntity<String> consultarRolUsuario(@PathVariable Integer idUsuario) {
        Usuario usuario = consultarUsuarioUseCase.consultarPorId(idUsuario);
        return ResponseEntity.ok(usuario.getRol().name());
    }

    @GetMapping("existeUsuario/{idUsuario}")
    public ResponseEntity<Boolean> existeUsuario(@PathVariable Integer idUsuario) {
        System.out.println(">>>>>>>>>>>>> ENTRA A CONSULTAR SI EXISTE ID : "+idUsuario);
        boolean existe = consultarUsuarioUseCase.existePorId(idUsuario);
        return ResponseEntity.ok(existe);
    }

}
