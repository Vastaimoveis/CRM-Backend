package com.VastaImoveis.CRM.Lead.Entity.dto;

import com.VastaImoveis.CRM.Lead.Entity.Domain.OrigemLead;

public class LeadPublicRequestDTO {
    private String nome;
    private String telefone;
    private String rendaMedia;
    private String empreendimento;
    private OrigemLead origem;

    public LeadPublicRequestDTO(String nome, String telefone, String rendaMedia, String empreendimento, OrigemLead origem) {
        this.nome = nome;
        this.telefone = telefone;
        this.rendaMedia = rendaMedia;
        this.empreendimento = empreendimento;
        this.origem = origem;
    }

    public LeadPublicRequestDTO(){};

    public String getEmpreendimento() {
        return empreendimento;
    }

    public void setEmpreendimento(String empreendimento) {
        this.empreendimento = empreendimento;
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

    public String getRendaMedia() {
        return rendaMedia;
    }

    public void setRendaMedia(String rendaMedia) {
        this.rendaMedia = rendaMedia;
    }

    public OrigemLead getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemLead origem) {
        this.origem = origem;
    }
}
