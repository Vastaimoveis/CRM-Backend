package com.VastaImoveis.CRM.Requisitos.Entity.dto;

import com.VastaImoveis.CRM.Requisitos.Entity.Domain.StatusRequisito;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequisitoResponseDTO {

    private final UUID id;
    private final User corretorId;
    private final User gerenteId;
    private final String title;
    private final String message;
    private final StatusRequisito status;
    private final LocalDateTime createdAt;

    public RequisitoResponseDTO(UUID id, User corretorId, User gerenteId, String title, String message, StatusRequisito status, LocalDateTime createdAt) {
        this.id = id;
        this.corretorId = corretorId;
        this.gerenteId = gerenteId;
        this.title = title;
        this.message = message;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public User getCorretorId() {
        return corretorId;
    }

    public User getGerenteId() {
        return gerenteId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public StatusRequisito getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
