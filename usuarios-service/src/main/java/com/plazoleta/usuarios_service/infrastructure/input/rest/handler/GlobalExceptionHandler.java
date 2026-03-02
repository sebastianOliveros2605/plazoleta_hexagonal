package com.plazoleta.usuarios_service.infrastructure.input.rest.handler;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.plazoleta.usuarios_service.domain.exception.CredencialesInvalidasException;
import com.plazoleta.usuarios_service.domain.exception.DatosInvalidosException;
import com.plazoleta.usuarios_service.domain.exception.EmailDuplicadoException;
import com.plazoleta.usuarios_service.domain.exception.MenorDeEdadException;
import com.plazoleta.usuarios_service.domain.exception.RestauranteNoEncontradoException;
import com.plazoleta.usuarios_service.domain.exception.RolNoEncontradoException;
import com.plazoleta.usuarios_service.domain.exception.UsuarioNoEncontradoException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            DatosInvalidosException.class,
            MenorDeEdadException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ApiErrorResponse> handleBadRequest(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, extractMessage(ex), request.getRequestURI());
    }

    @ExceptionHandler(CredencialesInvalidasException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(CredencialesInvalidasException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(EmailDuplicadoException.class)
    public ResponseEntity<ApiErrorResponse> handleConflict(EmailDuplicadoException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({ UsuarioNoEncontradoException.class, RolNoEncontradoException.class, RestauranteNoEncontradoException.class })
    public ResponseEntity<ApiErrorResponse> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String message, String path) {
        ApiErrorResponse body = new ApiErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path);
        return ResponseEntity.status(status).body(body);
    }

    private String extractMessage(Exception ex) {
        if (ex instanceof MethodArgumentNotValidException validationEx
                && validationEx.getBindingResult().hasFieldErrors()) {
            return validationEx.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        }
        return ex.getMessage();
    }
}
