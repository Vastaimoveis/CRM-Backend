package com.VastaImoveis.CRM.shared.utils;

public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private String errorCode;

    // Construtor para sucesso e respostas antigas
    public ApiResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errorCode = null;
    }

    // Construtor para erros identificados
    public ApiResponse(
            boolean success,
            T data,
            String message,
            String errorCode
    ) {
        this.success = success;
        this.data = data;
        this.message = message;
        this.errorCode = errorCode;
    }

    // getters e setters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}