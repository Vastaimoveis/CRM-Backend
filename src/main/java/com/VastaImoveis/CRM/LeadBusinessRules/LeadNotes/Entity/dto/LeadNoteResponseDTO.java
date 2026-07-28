package com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class LeadNoteResponseDTO {
    private final UUID id;
    private final UUID lead;
    private final String note;
    private final LocalDateTime createdAt;

    public LeadNoteResponseDTO(UUID id, UUID lead, String note, LocalDateTime createdAt) {
        this.id = id;
        this.lead = lead;
        this.note = note;
        this.createdAt = createdAt;
    }

    public UUID getLead() {
        return lead;
    }

    public UUID getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
