package com.VastaImoveis.CRM.shared.utils;

public class ApiResponse<T> {
    private final boolean success;
    private final T data;
    private  String errorCode;
    private final String text;
    public ApiResponse(boolean success,T data, String text) {
        this.success = success;
        this.data = data;
        this.text = text;
    }

    public T getData() {
        return data;
    }

    public String getText() {
        return text;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
