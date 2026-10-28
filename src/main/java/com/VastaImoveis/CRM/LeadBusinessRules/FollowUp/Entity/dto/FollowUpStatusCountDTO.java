package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto;

import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUpStatus;

public record FollowUpStatusCountDTO(
        FollowUpStatus status,
        Long quantidade
) {
}
