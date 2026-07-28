package com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.dto;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class LeadNoteRequestDTO {

    @NotBlank
    private String note;

    @NotNull(message = "LeadId é obrigatório")
    private UUID leadId;

    // Getters & Setters

    public String getNote() {
        return note;
    }

    public void setLeadId(UUID leadId) {
        this.leadId = leadId;
    }


    public UUID getLeadId() {
        return leadId;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UUID getLead() {
        return leadId;
    }

    public void setLeadByEntity(Lead lead) {
        this.leadId = lead.getId();
    }
}
