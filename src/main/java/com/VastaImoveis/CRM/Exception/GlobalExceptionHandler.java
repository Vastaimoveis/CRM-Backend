package com.VastaImoveis.CRM.Exception;


import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private ResponseEntity<ApiResponse<Object>> buildResponse(
            HttpStatus status,
            String message,
            Object data
    ) {
        return ResponseEntity.status(status)
                .body(
                        new ApiResponse<>(
                                false,
                                data,
                                message
                        )
                );
    }

    // 🔍 404
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                null,
                ErrorCode.RESOURCE_NOT_FOUND
        );
    }

    // ⚠️ 400 - regra de negócio
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Object>> handleBusiness(
            BusinessException ex,
            HttpServletRequest request
    ) {
        return buildResponse(
                ex.getHttpStatus(),
                ex.getMessage(),
                null,
                ex.getErrorCode()
        );
    }

    // 🧠 VALIDAÇÃO (ESSENCIAL)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
                .forEach(err ->
                        errors.put(err.getField(), err.getDefaultMessage())
                );

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Verifique os campos informados.",
                errors,
                ErrorCode.VALIDATION_ERROR
        );
    }

    // 🔥 fallback
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Erro interno", ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Tente novamente.",
                null,
                ErrorCode.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
        log.error("Credenciais inválidas: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Credenciais Inválidas",
                null
        );
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(
            Exception ex,
            HttpServletRequest request
    ) {
        log.warn("Acesso negado: {}", ex.getMessage());

        return buildResponse(
                HttpStatus.FORBIDDEN,
                "Você não possui permissão para realizar esta operação.",
                null,
                ErrorCode.ACCESS_DENIED
        );
    }

    @ExceptionHandler(InvalidEmailException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidEmail(InvalidCredentialsException ex, HttpServletRequest request) {
        log.error("Email não encontrado: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Email não encontrado",
                null
        );
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ApiResponse<Object>> handleInvalidPassword(InvalidCredentialsException ex, HttpServletRequest request) {
        log.error("Senha incorreta: {}", ex.getMessage());
        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Senha incorreta",
                null
        );
    }

    private ResponseEntity<ApiResponse<Object>> buildResponse(
            HttpStatus status,
            String message,
            Object data,
            ErrorCode errorCode
    ) {
        return ResponseEntity.status(status)
                .body(
                        new ApiResponse<>(
                                false,
                                data,
                                message,
                                errorCode.name()
                        )
                );
    }

}