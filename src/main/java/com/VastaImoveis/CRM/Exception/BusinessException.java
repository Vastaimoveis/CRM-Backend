package com.VastaImoveis.CRM.Exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;
    private final HttpStatus httpStatus;

    public BusinessException(String message) {
        this(message, ErrorCode.BUSINESS_RULE_VIOLATION, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(String message, ErrorCode errorCode) {
        this(message, errorCode, HttpStatus.BAD_REQUEST);
    }

    public BusinessException(
            String message,
            ErrorCode errorCode,
            HttpStatus httpStatus
    ) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}