package com.VastaImoveis.CRM.Notification.Mapper;

import com.VastaImoveis.CRM.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.Lead.mapper.LeadMapper;
import com.VastaImoveis.CRM.Notification.Entity.DTO.NotificationRequestDTO;
import com.VastaImoveis.CRM.Notification.Entity.DTO.NotificationResponseDTO;
import com.VastaImoveis.CRM.Notification.Entity.Domain.Notification;
import com.VastaImoveis.CRM.Notification.Entity.Domain.NotificationType;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.Users.mapper.UserMapper;

import java.time.LocalDateTime;

public class NotificationMapper {

    public static NotificationResponseDTO toDTO(Notification entity){

        return new NotificationResponseDTO(
                entity.getId(),
                LeadMapper.toDTO(entity.getLead()),
                UserMapper.toDTO(entity.getUser()),
                entity.getType(),
                entity.getRead(),
                entity.getCreatedAt(),
                entity.getAlarmAt()
                );
    }

    public static Notification toEntity(
            NotificationRequestDTO dto,
            Lead lead,
            User user
    ) {
        Notification notification = new Notification();
        notification.setLead(lead);
        notification.setUser(user);
        notification.setType(dto.getType());
        notification.setAlarmAt(dto.getAlarmAt());
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        return notification;
    }

}
