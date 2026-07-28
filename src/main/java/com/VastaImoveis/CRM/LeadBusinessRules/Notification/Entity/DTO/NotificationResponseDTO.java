package com.VastaImoveis.CRM.LeadBusinessRules.Notification.Entity.DTO;

import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.LeadResponseDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Notification.Entity.Domain.NotificationType;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationResponseDTO {

    private UUID id;

    private LeadResponseDTO lead;

    private UserResponseDTO user;

    private NotificationType type;

    private Boolean read;

    private LocalDateTime createdAt;

    private LocalDateTime alarmAt;

    public NotificationResponseDTO(UUID id, LeadResponseDTO lead, UserResponseDTO user, NotificationType type, Boolean read, LocalDateTime createdAt, LocalDateTime alarmAt) {
        this.id = id;
        this.lead = lead;
        this.user = user;
        this.type = type;
        this.read = read;
        this.createdAt = createdAt;
        this.alarmAt = alarmAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LeadResponseDTO getLead() {
        return lead;
    }

    public void setLead(LeadResponseDTO lead) {
        this.lead = lead;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
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

    public LocalDateTime getAlarmAt() {
        return alarmAt;
    }

    public void setAlarmAt(LocalDateTime alarmAt) {
        this.alarmAt = alarmAt;
    }
}
