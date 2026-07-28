package com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.OrigemLead;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.StatusLead;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LeadRequestDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min=3, max=100)
    private String nome;

    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    @Email(message = "Email inválido")
    private String email;

    private StatusLead status;

    @NotNull(message = "Origem é obrigatória")
    private OrigemLead origem;

    // Getters e Setters
    public OrigemLead getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemLead origem) {
        this.origem = origem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StatusLead getStatus() {
        return status;
    }

    public void setStatus(StatusLead status) {
        this.status = status;
    }

}
