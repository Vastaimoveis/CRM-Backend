package com.VastaImoveis.CRM.Notification.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.Lead.Repository.LeadRepository;
import com.VastaImoveis.CRM.Notification.Entity.DTO.NotificationReadDTO;
import com.VastaImoveis.CRM.Notification.Entity.DTO.NotificationRequestDTO;
import com.VastaImoveis.CRM.Notification.Entity.DTO.NotificationResponseDTO;
import com.VastaImoveis.CRM.Notification.Entity.Domain.Notification;
import com.VastaImoveis.CRM.Notification.Entity.Domain.NotificationType;
import com.VastaImoveis.CRM.Notification.Mapper.NotificationMapper;
import com.VastaImoveis.CRM.Notification.Repository.NotificationRepository;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.Users.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final NotificationRepository repository;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;

    public NotificationService(NotificationRepository repository, LeadRepository leadRepository, UserRepository userRepository) {
        this.repository = repository;
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
    }

    public List<NotificationResponseDTO> getByUser(UUID userId){

        return repository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(NotificationMapper::toDTO)
                .toList();

    }

    @Transactional
    public NotificationResponseDTO read(UUID id, NotificationReadDTO dto){

        Notification notification = repository.findById(id)
                .orElseThrow();

        notification.setRead(dto.getRead());

        return NotificationMapper.toDTO(repository.save(notification));

    }

    public NotificationResponseDTO create(NotificationRequestDTO dto) {

        Lead lead = leadRepository.findById(dto.getLeadId())
                .orElseThrow(() -> new BusinessException("Lead não encontrado"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        return switch (dto.getType()) {
            case REMINDER -> createReminder(
                    lead,
                    user,
                    dto.getAlarmAt()
            );

            case NEW_LEAD -> createNewLead(
                    lead,
                    user
            );
        };
    }

    public NotificationResponseDTO createNewLead(
            Lead lead,
            User user
    ) {

        return createNotification(
                lead,
                user,
                NotificationType.NEW_LEAD,
                null
        );
    }

    public NotificationResponseDTO createReminder(
            Lead lead,
            User user,
            LocalDateTime alarmAt
    ) {

        return createNotification(
                lead,
                user,
                NotificationType.REMINDER,
                alarmAt
        );
    }

    private NotificationResponseDTO createNotification(
            Lead lead,
            User user,
            NotificationType type,
            LocalDateTime alarmAt
    ) {

        Notification notification = new Notification();

        notification.setLead(lead);
        notification.setUser(user);
        notification.setType(type);
        notification.setAlarmAt(alarmAt);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification saved = repository.save(notification);

        return NotificationMapper.toDTO(saved);
    }



    public void delete(UUID id){
        repository.delete(repository.findById(id)
                .orElseThrow(() -> new BusinessException("Erro ao deletar notificação")));
    }

}
