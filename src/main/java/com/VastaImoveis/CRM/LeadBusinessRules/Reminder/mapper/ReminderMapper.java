package com.VastaImoveis.CRM.LeadBusinessRules.Reminder.mapper;

import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.domain.Reminder;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.dto.ReminderRequestDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.dto.ReminderResponseDTO;

public class ReminderMapper {
    public static ReminderResponseDTO toDTO(Reminder reminder){
        return new ReminderResponseDTO(
                reminder.getId(),
                reminder.getLead(),
                reminder.getUser(),
                reminder.getCreatedAt(),
                reminder.getAlarmAt(),
                reminder.getRead()

        );
    }

    public static Reminder fromDTO(ReminderRequestDTO dto){
        return new Reminder(dto.getAlarmAt());
    }
}
