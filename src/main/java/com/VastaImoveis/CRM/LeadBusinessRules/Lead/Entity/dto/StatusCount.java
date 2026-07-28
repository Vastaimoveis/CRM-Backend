package com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.StatusLead;

public class StatusCount {

    private StatusLead status;
    private Long count;

    public StatusCount(StatusLead status, Long count) {
        this.status = status;
        this.count = count;
    }

    public StatusLead getStatus() {
        return status;
    }

    public Long getCount() {
        return count;
    }
}