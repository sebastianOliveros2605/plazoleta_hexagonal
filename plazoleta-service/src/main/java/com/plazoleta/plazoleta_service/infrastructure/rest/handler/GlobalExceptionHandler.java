package com.plazoleta.plazoleta_service.infrastructure.rest.handler;

import com.plazoleta.plazoleta_service.domain.exception.CampoObligatorioException;
import com.plazoleta.plazoleta_service.domain.exception.NitInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.NoEsPropietarioException;
import com.plazoleta.plazoleta_service.domain.exception.NombreInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.PedidoInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.PedidoNoAutorizadoException;
import com.plazoleta.plazoleta_service.domain.exception.PedidoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.PlatoNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.PrecioInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.RestauranteNoExisteException;
import com.plazoleta.plazoleta_service.domain.exception.RolUsuarioNoPermitidoException;
import com.plazoleta.plazoleta_service.domain.exception.TelefonoInvalidoException;
import com.plazoleta.plazoleta_service.domain.exception.TransicionEstadoNoPermitidaException;
import com.plazoleta.plazoleta_service.domain.exception.UsuarioNoAsociadoRestauranteException;
import com.plazoleta.plazoleta_service.domain.exception.UsuarioNoExisteException;
import com.plazoleta.plazoleta_service.infrastructure.rest.dto.response.ErrorResponse;
import java.util.stream.Collectors;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CampoObligatorioException.class,
            NitInvalidoException.class,
            NombreInvalidoException.class,
            TelefonoInvalidoException.class,
            PrecioInvalidoException.class,
            PedidoInvalidoException.class,
            TransicionEstadoNoPermitidaException.class,
            UsuarioNoAsociadoRestauranteException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({
            RolUsuarioNoPermitidoException.class,
            NoEsPropietarioException.class,
            PedidoNoAutorizadoException.class
    })
    public ResponseEntity<ErrorResponse> handleForbidden(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({
            UsuarioNoExisteException.class,
            RestauranteNoExisteException.class,
            PlatoNoExisteException.class,
            PedidoNoExisteException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(message));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }
}
