package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto;

import java.util.List;

public record FollowUpSummaryDTO(
        List<FollowUpStatusCountDTO> statusCounts,
        Long todayCount
) {}