package com.VastaImoveis.CRM.Lead.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.Lead.Entity.dto.LeadPublicRequestDTO;
import com.VastaImoveis.CRM.Lead.Entity.dto.LeadResponseDTO;
import com.VastaImoveis.CRM.Lead.Repository.LeadRepository;
import com.VastaImoveis.CRM.Lead.mapper.LeadMapper;
import com.VastaImoveis.CRM.LeadNotes.Entity.domain.LeadNote;
import com.VastaImoveis.CRM.LeadNotes.Entity.dto.LeadNoteRequestDTO;
import com.VastaImoveis.CRM.LeadNotes.mapper.LeadNoteMapper;
import com.VastaImoveis.CRM.LeadNotes.repository.LeadNoteRepository;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.Users.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PublicLeadService {
    private final LeadRepository repository;
    private final LeadNoteRepository leadNoteRepository;
    private final UserRepository userRepository;
    @Value("${LEAD_DEFAULT_OWNER}")
    private String defaultLeadOwner;

    public PublicLeadService(LeadRepository repository, LeadNoteRepository leadNoteRepository, UserRepository userRepository) {
        this.repository = repository;
        this.leadNoteRepository = leadNoteRepository;
        this.userRepository = userRepository;
    }

    private void createNote(Lead lead, String note) {
        LeadNote leadNote = new LeadNote();
        leadNote.setLead(lead);
        leadNote.setNote(note);

        leadNoteRepository.save(leadNote);
    }

    @Transactional
    public LeadResponseDTO publicCreate(LeadPublicRequestDTO dto) {
        if (repository.existsByTelefone(dto.getTelefone())){
            throw new BusinessException("Telefone já cadastrado");
        }
        User user = userRepository.findByEmail(defaultLeadOwner)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        Lead lead = LeadMapper.toEntityPublic(dto);
        lead.setUser(user);
        Lead saved = repository.save(lead);

        if(!dto.getRendaMedia().isBlank()){
        createNote(saved, "Renda entre: " + dto.getRendaMedia());
        }

        if(!dto.getEmpreendimento().isBlank()){
            createNote(saved, "Empreendimento interessado: " + dto.getEmpreendimento());
        }

        return LeadMapper.toDTO(saved);
    }
}
