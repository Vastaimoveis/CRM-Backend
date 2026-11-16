package com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto;

import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Domain.ScriptCategories;

public class ScriptFilterDTO {
    private ScriptCategories category;

    public ScriptFilterDTO(ScriptCategories category) {
        this.category = category;
    }

    public ScriptCategories getCategory() {
        return category;
    }

    public void setCategory(ScriptCategories category) {
        this.category = category;
    }
}
