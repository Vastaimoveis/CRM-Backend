package com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto;


import java.util.UUID;

public record LeadPorCorretorCount(
        UUID corretorId,
        String corretorNome,
        Long quantidade
) {}