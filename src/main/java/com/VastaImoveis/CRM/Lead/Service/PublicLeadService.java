package com.VastaImoveis.CRM.Lead.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.Lead.Entity.dto.LeadPublicRequestDTO;
import com.VastaImoveis.CRM.Lead.Entity.dto.LeadResponseDTO;
import com.VastaImoveis.CRM.Lead.Repository.LeadRepository;
import com.VastaImoveis.CRM.Lead.mapper.LeadMapper;
import com.VastaImoveis.CRM.LeadNotes.Entity.dto.LeadNoteRequestDTO;
import com.VastaImoveis.CRM.LeadNotes.mapper.LeadNoteMapper;
import com.VastaImoveis.CRM.LeadNotes.repository.LeadNoteRepository;
import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.Users.Repository.UserRepository;
import com.VastaImoveis.CRM.shared.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class PublicLeadService {
    private final LeadRepository repository;
    private final LeadNoteRepository leadNoteRepository;
    private final UserRepository userRepository;
    public PublicLeadService(LeadRepository repository, LeadNoteRepository leadNoteRepository, UserRepository userRepository) {
        this.repository = repository;
        this.leadNoteRepository = leadNoteRepository;
        this.userRepository = userRepository;
    }

    public LeadResponseDTO publicCreate(LeadPublicRequestDTO dto) {
        if (repository.existsByTelefone(dto.getTelefone())){
            throw new BusinessException("Telefone já cadastrado");
        }

        User user = userRepository.findByEmail("thiago.silverio@vastaimoveis.com.br")
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        Lead lead = LeadMapper.toEntityPublic(dto);
        lead.setUser(user);
        Lead saved = repository.save(lead);
        LeadNoteRequestDTO leadNote = new LeadNoteRequestDTO();
        leadNote.setLeadId(saved.getId());
        leadNote.setNote("Faixa de renda entre: " + dto.getRendaMedia());
        leadNoteRepository.save(LeadNoteMapper.toEntity(leadNote));
        return LeadMapper.toDTO(saved);
    }
}
