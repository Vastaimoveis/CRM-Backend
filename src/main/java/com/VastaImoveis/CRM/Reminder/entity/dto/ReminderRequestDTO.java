package com.VastaImoveis.CRM.Reminder.entity.dto;

import com.VastaImoveis.CRM.Reminder.entity.domain.ReminderType;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReminderRequestDTO {
        @NotBlank(message = "Lead obrigatório")
        private UUID leadId;

        @NotBlank(message = "Usuário obrigatório")
        private UUID userId;;

        @NotBlank(message = "data obrigatório")
        private LocalDateTime alarmAt;

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

    public LocalDateTime getAlarmAt() {
        return alarmAt;
    }

    public void setAlarmAt(LocalDateTime alarmAt) {
        this.alarmAt = alarmAt;
    }
}
