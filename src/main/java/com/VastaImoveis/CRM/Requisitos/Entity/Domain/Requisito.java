package com.VastaImoveis.CRM.Requisitos.Entity.Domain;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "requirements")
public class Requisito {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String message;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private StatusRequisito status;

    @ManyToOne
    @JoinColumn(name = "corretor_id")
    private User corretor;

    @ManyToOne
    @JoinColumn(name = "gerente_id")
    private User gerente;

    public Requisito(){}

    @PrePersist
    public void prePersist(){
        this.createdAt = LocalDateTime.now();
        this.status = StatusRequisito.PENDENTE;
    }

    public UUID getId() {
        return id;
    }

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public StatusRequisito getStatus() {
        return status;
    }

    public void setStatus(StatusRequisito status) {
        this.status = status;
    }

    public User getCorretorId() {
        return corretor;
    }

    public void setCorretorId(User corretorId) {
        this.corretor = corretorId;
    }

    public User getGerenteId() {
        return gerente;
    }

    public void setGerenteId(User gerenteId) {
        this.gerente = gerenteId;
    }
}
