package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "follow_ups")
public class FollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private Integer followUpAtual;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lead_id", nullable = false)
    private Lead lead;

    @Column(nullable = false)
    private Integer followUpTotal;

    @Column
    private LocalDateTime ultimoContato;

    @Column(nullable = false)
    private LocalDateTime proximoContato;

    @Column
    private Integer diasEmAtraso;

    @Column(nullable = false)
    private boolean followUpChecked = false;

    @Column
    private Integer diasAtrasoNoFechamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FollowUpStatus status = FollowUpStatus.PENDENTE;

    @Column(length = 2000)
    private String observacao;

    @Column(nullable = false)
    private boolean atual;

    @Column
    private LocalDateTime concluidoEm;

    @Column
    private LocalDateTime encerradoEm;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public FollowUp() {
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setLead(Lead lead) {
        this.lead = lead;
    }

    public Lead getLead() {
        return lead;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Integer getDiasAtrasoNoFechamento() {
        return diasAtrasoNoFechamento;
    }

    public void setDiasAtrasoNoFechamento(Integer diasAtrasoNoFechamento) {
        this.diasAtrasoNoFechamento = diasAtrasoNoFechamento;
    }

    public FollowUpStatus getStatus() {
        return status;
    }

    public void setStatus(FollowUpStatus status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Integer getFollowUpAtual() {
        return followUpAtual;
    }

    public void setFollowUpAtual(Integer followUpAtual) {
        this.followUpAtual = followUpAtual;
    }

    public Integer getFollowUpTotal() {
        return followUpTotal;
    }

    public void setFollowUpTotal(Integer followUpTotal) {
        this.followUpTotal = followUpTotal;
    }

    public LocalDateTime getUltimoContato() {
        return ultimoContato;
    }

    public void setUltimoContato(LocalDateTime ultimoContato) {
        this.ultimoContato = ultimoContato;
    }

    public LocalDateTime getProximoContato() {
        return proximoContato;
    }

    public void setProximoContato(LocalDateTime proximoContato) {
        this.proximoContato = proximoContato;
    }

    public Integer getDiasEmAtraso() {
        return diasEmAtraso;
    }

    public void setDiasEmAtraso(Integer diasEmAtraso) {
        this.diasEmAtraso = diasEmAtraso;
    }

    public boolean isFollowUpChecked() {
        return followUpChecked;
    }

    public void setFollowUpChecked(boolean followUpChecked) {
        this.followUpChecked = followUpChecked;
    }

    public LocalDateTime getConcluidoEm() {
        return concluidoEm;
    }

    public void setConcluidoEm(LocalDateTime concluidoEm) {
        this.concluidoEm = concluidoEm;
    }

    public boolean isAtual() {
        return atual;
    }

    public void setAtual(boolean atual) {
        this.atual = atual;
    }

    public LocalDateTime getEncerradoEm() {
        return encerradoEm;
    }

    public void setEncerradoEm(LocalDateTime encerradoEm) {
        this.encerradoEm = encerradoEm;
    }
}
