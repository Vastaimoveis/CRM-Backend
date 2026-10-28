package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto;

import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record FollowUpResponseDTO(
        UUID id,
        LeadSummary lead,

        Integer followUpAtual,
        Integer followUpTotal,

        LocalDateTime proximoContato,
        LocalDateTime ultimoContato,
        LocalDateTime concluidoEm,
        LocalDateTime encerradoEm,
        LocalDateTime createdAt,

        Integer diasEmAtraso,
        FollowUpStatus status,
        String observacao
) {
}
