package com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.dto;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReminderResponseDTO {

    private final UUID id;
    private final Lead lead;
    private final User user;
    private final LocalDateTime createdAt;
    private final LocalDateTime alarmAt;
    private final Boolean read;

    public ReminderResponseDTO(UUID id, Lead lead, User user, LocalDateTime createdAt, LocalDateTime alarmAt, Boolean read) {
        this.id = id;
        this.lead = lead;
        this.user = user;
        this.createdAt = createdAt;
        this.alarmAt = alarmAt;
        this.read = read;
    }

    public UUID getId() {
        return id;
    }

    public Lead getLead() {
        return lead;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getAlarmAt() {
        return alarmAt;
    }

    public Boolean getRead() {
        return read;
    }
}
