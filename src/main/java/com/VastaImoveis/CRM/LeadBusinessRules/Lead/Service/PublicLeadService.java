package com.VastaImoveis.CRM.LeadBusinessRules.Lead.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.LeadPublicRequestDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.LeadResponseDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Repository.LeadRepository;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.mapper.LeadMapper;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.domain.LeadNote;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.repository.LeadNoteRepository;
import com.VastaImoveis.CRM.LeadBusinessRules.Notification.Service.NotificationService;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PublicLeadService {
    private final LeadRepository repository;
    private final LeadNoteRepository leadNoteRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    @Value("${LEAD_DEFAULT_OWNER}")
    private String defaultLeadOwner;

    public PublicLeadService(LeadRepository repository, LeadNoteRepository leadNoteRepository, UserRepository userRepository, NotificationService notificationService) {
        this.repository = repository;
        this.leadNoteRepository = leadNoteRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    private void createNote(Lead lead, String note) {
        LeadNote leadNote = new LeadNote();
        leadNote.setLead(lead);
        leadNote.setNote(note);

        leadNoteRepository.save(leadNote);
    }

    @Transactional
    public LeadResponseDTO publicCreate(LeadPublicRequestDTO dto) {

        User user = userRepository.findByEmail(defaultLeadOwner)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));

        Lead lead = repository.findByTelefone(dto.getTelefone()).orElse(null);

        if (lead == null) {
            lead = LeadMapper.toEntityPublic(dto);
            lead.setUser(user);
            lead = repository.save(lead);

            notificationService.createNewLead(lead, user);
        }

        if (dto.getRendaMedia() != null && !dto.getRendaMedia().isBlank()) {
            createNote(lead, "Renda entre: " + dto.getRendaMedia());
        }

        if (dto.getEmpreendimento() != null && !dto.getEmpreendimento().isBlank()) {
            createNote(lead, "Empreendimento interessado: " + dto.getEmpreendimento());
        }

        return LeadMapper.toDTO(lead);
    }
}
