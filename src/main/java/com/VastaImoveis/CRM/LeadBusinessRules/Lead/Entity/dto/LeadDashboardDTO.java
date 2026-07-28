package com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto;

import java.util.Map;

public class LeadDashboardDTO {

    private long total;
    private Map<String, Long> porStatus;

    public LeadDashboardDTO(long total, Map<String, Long> porStatus) {
        this.total = total;
        this.porStatus = porStatus;
    }

    public long getTotal() {
        return total;
    }

    public Map<String, Long> getPorStatus() {
        return porStatus;
    }
}