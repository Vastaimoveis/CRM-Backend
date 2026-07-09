package com.VastaImoveis.CRM.Lead.Entity.dto;

import com.VastaImoveis.CRM.Lead.Entity.Domain.OrigemLead;
import com.VastaImoveis.CRM.Lead.Entity.Domain.StatusLead;

import java.time.LocalDateTime;
import java.util.UUID;

public class LeadResponseDTO {

    private final UUID id;
    private final UUID userId;
    private final String nome;
    private final String telefone;
    private final String email;
    private final StatusLead status;
    private boolean hasNotes;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
    private final OrigemLead origem;

    public boolean isHasNotes() {
        return hasNotes;
    }

    public UUID getUserId() {
        return userId;
    }

    public LeadResponseDTO(UUID id, UUID userId, String nome, String telefone, String email,
                           StatusLead status,
                           LocalDateTime createdAt, LocalDateTime updatedAt,
                            OrigemLead origem) {
        this.id = id;
        this.userId = userId;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.origem = origem;
    }

    // Getters
    public void setHasNotes(boolean has) {
        this.hasNotes = has;
    }
    public UUID getId() {
        return id;
    }

    public OrigemLead getOrigem() {
        return origem;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getEmail() {
        return email;
    }

    public StatusLead getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
