package com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto;

import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Domain.ScriptCategories;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ScriptCreateDTO {
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    private String title;

    @NotBlank(message = "Mensagem é obrigatória")
    @Size(max = 5000, message = "A mensagem deve ter no máximo 5000 caracteres")
    private String message;

    @NotNull
    private ScriptCategories category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ScriptCategories getCategory() {
        return category;
    }

    public void setCategory(ScriptCategories category) {
        this.category = category;
    }
}
