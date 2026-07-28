package com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.domain;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "Anotacoes")
public class LeadNote {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "anotacao")
    private String note;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lead_id")
    public Lead lead;

    public UUID getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Lead getLead() {
        return lead;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    //Construtor padrão obrigatório
    public LeadNote () {}

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
    }
}
