package com.VastaImoveis.CRM.Exception;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Email não encontrado.");
    }
}
