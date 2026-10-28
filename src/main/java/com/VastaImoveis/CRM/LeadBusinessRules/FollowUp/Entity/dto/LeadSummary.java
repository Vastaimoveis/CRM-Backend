package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.StatusLead;

import java.util.UUID;

public record LeadSummary(
        UUID Id,
        String nome,
        String telefone,
        UUID userId,
        StatusLead status
) {
}
