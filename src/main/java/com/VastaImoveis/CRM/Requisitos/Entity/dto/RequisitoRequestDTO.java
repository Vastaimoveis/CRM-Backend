package com.VastaImoveis.CRM.Requisitos.Entity.dto;

import com.VastaImoveis.CRM.Requisitos.Entity.Domain.StatusRequisito;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class RequisitoRequestDTO {

    @NotBlank(message = "Title é obrigatório")
    private String title;

    @NotBlank(message = "O texto é obrigatório")
    private String message;

    private StatusRequisito status;

    private UUID corretor;

    private UUID gerente;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StatusRequisito getStatus() {
        return status;
    }

    public void setStatus(StatusRequisito status) {
        this.status = status;
    }

    public UUID getCorretor() {
        return corretor;
    }

    public void setCorretor(UUID corretor) {
        this.corretor = corretor;
    }

    public UUID getGerente() {
        return gerente;
    }

    public void setGerente(UUID gerente) {
        this.gerente = gerente;
    }
}
