package com.VastaImoveis.CRM.Reminder.entity.domain;

import com.VastaImoveis.CRM.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Reminder {

    @Id
    @GeneratedValue
    private UUID id;

    private Boolean read = false;

    private LocalDateTime createdAt;

    private LocalDateTime alarmAt;

    @ManyToOne
    @JsonIgnore
    private Lead lead;

    @ManyToOne
    @JsonIgnore
    private User user;

    public Reminder(){}

    public Reminder(LocalDateTime alarmAt) {
        this.alarmAt = alarmAt;
    }
    @PrePersist
    public void PrePersist(){
        this.createdAt = LocalDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getAlarmAt() {
        return alarmAt;
    }

    public void setAlarmAt(LocalDateTime alarmAt) {
        this.alarmAt = alarmAt;
    }
}