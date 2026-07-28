package com.example.backend.exception;

import com.example.backend.constants.GlobalErrorMessages;
import com.example.backend.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildResponse(
            HttpStatus status,
            String error,
            Object message) {

        ErrorResponse response = ErrorResponse.builder()
                .status(status.value())
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                GlobalErrorMessages.NO_ENCONTRADO,
                ex.getMessage()
        );
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(ResourceAlreadyExistsException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                GlobalErrorMessages.CONFLICTO,
                ex.getMessage()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                GlobalErrorMessages.SOLICITUD_INVALIDA,
                ex.getMessage()
        );
    }

    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleJwtAuthentication(JwtAuthenticationException ex) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                GlobalErrorMessages.NO_AUTORIZADO,
                ex.getMessage()
        );
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthentication(AuthenticationException ex) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                GlobalErrorMessages.NO_AUTORIZADO,
                ex.getMessage()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return buildResponse(
                HttpStatus.UNAUTHORIZED,
                GlobalErrorMessages.NO_AUTORIZADO,
                "Usuario o contraseña incorrectos."
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
        return buildResponse(
                HttpStatus.FORBIDDEN,
                GlobalErrorMessages.PROHIBIDO,
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {

        Map<String, String> errores = new LinkedHashMap<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errores.put(error.getField(), error.getDefaultMessage());
        }

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                GlobalErrorMessages.SOLICITUD_INVALIDA,
                errores
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                GlobalErrorMessages.SOLICITUD_INVALIDA,
                "Valor inválido para el parámetro: " + ex.getName()
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                GlobalErrorMessages.ERROR_PROCESO,
                ex.getMessage()
        );
    }
}