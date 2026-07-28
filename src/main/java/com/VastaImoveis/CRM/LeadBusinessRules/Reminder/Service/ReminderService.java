package com.VastaImoveis.CRM.LeadBusinessRules.Reminder.Service;

import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.Repository.ReminderRepository;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.domain.Reminder;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.dto.ReminderRequestDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.entity.dto.ReminderResponseDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Reminder.mapper.ReminderMapper;
import com.VastaImoveis.CRM.Exception.ResourceNotFoundException;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Repository.LeadRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReminderService {
    private final ReminderRepository repository;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;

    public ReminderService(ReminderRepository repository, LeadRepository leadRepository, UserRepository userRepository) {
        this.repository = repository;
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
    }

    public ReminderResponseDTO create(ReminderRequestDTO dto){
        Lead lead = leadRepository.findById(dto.getLeadId())
                .orElseThrow(() -> new ResourceNotFoundException("Lead não encontrado"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User não encontrado"));

        Reminder reminder = ReminderMapper.fromDTO(dto);
        reminder.setLead(lead);
        reminder.setUser(user);

        return ReminderMapper.toDTO(repository.save(reminder));
    }

    public List<ReminderResponseDTO> findByUserId(UUID userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lead não encontrado"));
        return repository.findByUserOrderByAlarmAtDesc(user).stream().map(ReminderMapper::toDTO).toList();
    }

    public ReminderResponseDTO patchRead(UUID AlertId, Boolean read){
        Reminder reminder = repository.findById(AlertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));
        reminder.setRead(read);
        return ReminderMapper.toDTO(repository.save(reminder));
    }

    public void delete(UUID AlertId){
        Reminder reminder = repository.findById(AlertId)
                .orElseThrow(() -> new ResourceNotFoundException("Alerta não encontrado"));
        repository.delete(reminder);
    }
}
