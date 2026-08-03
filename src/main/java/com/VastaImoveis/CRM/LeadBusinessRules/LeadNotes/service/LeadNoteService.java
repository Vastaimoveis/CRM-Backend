package com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.Exception.ResourceNotFoundException;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Repository.LeadRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;
import com.VastaImoveis.CRM.shared.utils.SecurityUtils;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.domain.LeadNote;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.dto.LeadNoteRequestDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.Entity.dto.LeadNoteResponseDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.mapper.LeadNoteMapper;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.repository.LeadNoteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LeadNoteService {

    private final LeadNoteRepository repository;
    private final LeadRepository leadRepository;

    public LeadNoteService(LeadNoteRepository repository, LeadRepository leadRepository) {
        this.repository = repository;
        this.leadRepository = leadRepository;
    }

    //Criar anotação
    public LeadNoteResponseDTO create(LeadNoteRequestDTO dto) {
        Lead lead = leadRepository.findById(dto.getLead()).orElseThrow(() -> new ResourceNotFoundException("Lead não encontrado"));
        if (!lead.getUser().getId().equals(SecurityUtils.getCurrentUser().getId())) {
            throw new BusinessException("Você não tem permissão para adicionar nota neste lead");
        }
        LeadNote leadNote = LeadNoteMapper.toEntity(dto);
        leadNote.setLead(lead);
        LeadNote saved = repository.save(leadNote);

        return LeadNoteMapper.toDTO(saved);
    }

    //Listar por lead
    public Page<LeadNoteResponseDTO> findByLead(Lead lead, Pageable pageable) {
        return repository.findByLead(lead, pageable).map(LeadNoteMapper::toDTO);
    }

    public Page<LeadNoteResponseDTO> findByLeadId(UUID id, Pageable pageable) {
        return repository.findByLeadIdOrderByCreatedAtDesc(id, pageable).map(LeadNoteMapper::toDTO);
    }

    //Buscar por ID
    public LeadNoteResponseDTO findById(UUID id) {
        LeadNote leadNote = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotação não encontrada"));

        return LeadNoteMapper.toDTO(leadNote);
    }

    //Deletar
    public void delete(UUID id) {

        LeadNote leadNote = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Anotação não encontrada"));

        repository.delete(leadNote);
    }
}
