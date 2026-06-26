package com.VastaImoveis.CRM.shared.utils;

public class ApiResponse<T> {
    private final boolean success;
    private final T data;
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
}
