package com.VastaImoveis.CRM.LeadBusinessRules.Notification.Entity.DTO;



import com.VastaImoveis.CRM.LeadBusinessRules.Notification.Entity.Domain.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationRequestDTO {

    private UUID leadId;
    private UUID userId;
    private NotificationType type;
    private LocalDateTime alarmAt;

    public NotificationRequestDTO() {
    }

    public NotificationRequestDTO(
            UUID leadId,
            UUID userId,
            NotificationType type,
            LocalDateTime alarmAt
    ) {
        this.leadId = leadId;
        this.userId = userId;
        this.type = type;
        this.alarmAt = alarmAt;
    }

    public UUID getLeadId() {
        return leadId;
    }

    public void setLeadId(UUID leadId) {
        this.leadId = leadId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public LocalDateTime getAlarmAt() {
        return alarmAt;
    }

    public void setAlarmAt(LocalDateTime alarmAt) {
        this.alarmAt = alarmAt;
    }
}
