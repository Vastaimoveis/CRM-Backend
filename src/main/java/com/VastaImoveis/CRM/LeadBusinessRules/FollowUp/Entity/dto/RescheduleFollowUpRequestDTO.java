package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto;

import java.time.LocalDateTime;

public record RescheduleFollowUpRequestDTO(
        LocalDateTime proximoContato,
        String observacao
) {
}
