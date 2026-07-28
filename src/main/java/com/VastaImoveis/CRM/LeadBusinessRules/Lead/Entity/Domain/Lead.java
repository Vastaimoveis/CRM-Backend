package com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain;

import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.domain.LeadNote;
import com.VastaImoveis.CRM.LeadBusinessRules.Notification.Entity.Domain.Notification;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "leads")
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(unique = true, nullable = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusLead status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrigemLead origem;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<LeadNote> notes;

    @OneToMany(mappedBy = "lead", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Notification> notifications;

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<LeadNote> getNotes() {
        return notes;
    }

    public void setNotes(List<LeadNote> notes) {
        this.notes = notes;
    }

    // 🔥 Construtor padrão (obrigatório pro JPA)
    public Lead() {}

    // 🔥 Hook automático antes de persistir
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.status = (this.status == null) ? StatusLead.CADASTRADO : this.status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // 🔥 Hook automático antes de atualizar
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Getters e Setters

    public UUID getId() {
        return id;
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
        this.email = ( email ==null || email.isBlank() ) ? null : email.trim().toLowerCase();
    }

    public StatusLead getStatus() {
        return status;
    }

    public void setStatus(StatusLead status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OrigemLead getOrigem() {
        return origem;
    }

    public void setOrigem(OrigemLead origem) {
        this.origem = origem;
    }
}