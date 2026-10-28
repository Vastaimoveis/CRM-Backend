package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateFollowUpRequest(
        @Size(min = 5, max = 2000)
        String observacao
) { }

