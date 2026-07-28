package com.VastaImoveis.CRM.LeadBusinessRules.Lead.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.StatusLead;
import com.VastaImoveis.CRM.Lead.Entity.dto.*;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.LeadDashboardDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.LeadRequestDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.LeadResponseDTO;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.dto.StatusCount;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Repository.LeadRepository;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.mapper.LeadMapper;
import com.VastaImoveis.CRM.LeadBusinessRules.LeadNotes.repository.LeadNoteRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Repository.UserRepository;
import com.VastaImoveis.CRM.shared.utils.SecurityUtils;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class LeadService {

    private final LeadRepository repository;
    private final LeadNoteRepository leadNoteRepository;
    private final UserRepository userRepository;
    public LeadService(LeadRepository repository, LeadNoteRepository leadNoteRepository, UserRepository userRepository) {
        this.repository = repository;
        this.leadNoteRepository = leadNoteRepository;
        this.userRepository = userRepository;
    }

    public LeadResponseDTO create(LeadRequestDTO dto) {
        if (repository.existsByEmail(dto.getEmail()) && !dto.getEmail().isEmpty()) {
            throw new BusinessException("Email já cadastrado");
        }
        if(repository.existsByTelefone(dto.getTelefone())){
            throw new BusinessException("Telefone já cadastrado");
        }

        User user = SecurityUtils.getCurrentUser();
        Lead lead = LeadMapper.toEntity(dto);
        lead.setUser(user);
        Lead saved = repository.save(lead);
        return LeadMapper.toDTO(saved);
    }


    public Page<LeadResponseDTO> findAllWithPage(Pageable pageable) {

        User user = SecurityUtils.getCurrentUser();

        if (user.getRole().name().equals("GERENTE")) {
            Page<Lead> page = repository.findAll(pageable);
            List<UUID> leadIds = page.getContent()
                    .stream().map(Lead::getId).toList();
            Set<UUID> leadsWithNotes = new HashSet<>(leadNoteRepository.findLeadIdsWithNotes(leadIds));
            return page.map(lead -> {
                LeadResponseDTO dto = LeadMapper.toDTO(lead);
                dto.setHasNotes(leadsWithNotes.contains(lead.getId()));
                return dto;
            });
        }
        Page<Lead> page = repository.findByUser(user, pageable);
        List<UUID> leadIds = page.getContent()
                .stream().map(Lead::getId).toList();
        Set<UUID> leadsWithNotes = new HashSet<>(leadNoteRepository.findLeadIdsWithNotes(leadIds));
        return page.map(lead -> {
            LeadResponseDTO dto = LeadMapper.toDTO(lead);
            dto.setHasNotes(leadsWithNotes.contains(lead.getId()));
            return dto;
        });
    };

    public Page<LeadResponseDTO> findByStatus(Pageable pageable, StatusLead status) {
        User user = SecurityUtils.getCurrentUser();

        if(!user.getRole().name().equals("GERENTE") && status.equals(StatusLead.ENCERRADO)){
            throw new BusinessException("Você não tem acesso a essa chamada");
        }

        Page<Lead> page = repository.findByStatus(status, pageable);
        List<UUID> leadIds = page.getContent()
                .stream().map(Lead::getId).toList();
        Set<UUID> leadsWithNotes = new HashSet<>(leadNoteRepository.findLeadIdsWithNotes(leadIds));
        return page.map(lead -> {
            LeadResponseDTO dto = LeadMapper.toDTO(lead);
            dto.setHasNotes(leadsWithNotes.contains(lead.getId()));
            return dto;
        });
    }

    public Page<LeadResponseDTO> findAllNotEncerrado(Pageable pageable){
        Page<Lead> page = repository.findByStatusNot(StatusLead.ENCERRADO, pageable);
        List<UUID> leadIds = page.getContent()
                .stream().map(Lead::getId).toList();
        Set<UUID> leadsWithNotes = new HashSet<>(leadNoteRepository.findLeadIdsWithNotes(leadIds));
        return page.map(lead -> {
            LeadResponseDTO dto = LeadMapper.toDTO(lead);
            dto.setHasNotes(leadsWithNotes.contains(lead.getId()));
            return dto;
        });
    }

    public Page<LeadResponseDTO> findAllNotEncerradoByUser(UUID userId, Pageable pageable) {
        Page<Lead> page = repository.findByStatusNotAndUser_Id(
                StatusLead.ENCERRADO,
                userId,
                pageable
        );

        List<UUID> leadIds = page.getContent()
                .stream()
                .map(Lead::getId)
                .toList();

        Set<UUID> leadsWithNotes = new HashSet<>(
                leadNoteRepository.findLeadIdsWithNotes(leadIds)
        );

        return page.map(lead -> {
            LeadResponseDTO dto = LeadMapper.toDTO(lead);
            dto.setHasNotes(leadsWithNotes.contains(lead.getId()));
            return dto;
        });
    }

    public Page<LeadResponseDTO> filter(
            String search,
            StatusLead status,
            UUID userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {
        LocalDateTime start =
                startDate != null
                        ? startDate.atStartOfDay()
                        : LocalDateTime.of(1900,1,1,0,0);

        LocalDateTime end =
                endDate != null
                        ? endDate.plusDays(1).atStartOfDay()
                        : LocalDateTime.of(2999,12,31,23,59);


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("Erro ao buscar usuário no filter"));

        boolean isGerente = user.getRole().name().equals("GERENTE");

        Page<Lead> page = repository
                .filter(search, status, isGerente ? null : userId, start, end, pageable);
        List<UUID> listIds = page.getContent()
                .stream()
                .map(Lead::getId)
                .toList();
        Set<UUID> leadsWithNotes =
                new HashSet<>(leadNoteRepository.findLeadIdsWithNotes(listIds));

        return page.map(lead -> {
                    LeadResponseDTO dto = LeadMapper.toDTO(lead);
                    dto.setHasNotes(
                            leadsWithNotes.contains(lead.getId())
                    );
                    return dto;
        }
        );
    }

    public List<LeadResponseDTO> findOportunidades(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado em findOportunidades"));
        List<StatusLead> statusPermitidos = List.of(
                StatusLead.ATENDIMENTO,
                StatusLead.AGUARDANDO,
                StatusLead.VISITA,
                StatusLead.NEGOCIACAO,
                StatusLead.VENDA
        );

        List<Lead> leads;

        if(user.getRole().name().equals("GERENTE")){
            leads = repository.findByStatusIn(statusPermitidos);
        } else {
            leads = repository.findByStatusInAndUserId(
                    statusPermitidos,
                    user.getId()
            );
        }

        return leads.stream()
                .map(LeadMapper::toDTO)
                .toList();
    }

    public Page<LeadResponseDTO> findBySearch(Pageable pageable, String search){
        return repository.search(search, pageable).map(LeadMapper::toDTO);
    }

    public List<LeadResponseDTO> findAllByUserIdList(UUID id){
        return repository.findByUserId(id).stream().map(LeadMapper::toDTO).toList();
    }

    public Page<LeadResponseDTO> findAllByUser(UUID userId, Pageable pageable){
        User user = SecurityUtils.getCurrentUser();
        if(!user.getRole().name().equals("Gerente")){
            throw new BusinessException("Você não tem permissão para essa chamada");
        }
        return repository.findByUserId(userId, pageable).map(LeadMapper::toDTO);
    }

    public LeadResponseDTO findById(UUID id) {
        User user = SecurityUtils.getCurrentUser();
        Lead lead = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Lead não encontrado"));
        if (user.getRole().name().equals("CORRETOR") &&
                !lead.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Você não pode acessar este lead");
        }

        return LeadMapper.toDTO(lead);

    }

    public LeadResponseDTO update(UUID id, LeadRequestDTO dto) {
        User user = SecurityUtils.getCurrentUser();

        Lead lead = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Lead não encontrado"));

        if (user.getRole().name().equals("CORRETOR") &&
                !lead.getUser().getId().equals(user.getId())) {
            throw new BusinessException("Você não pode editar este Lead");
        }

        if(lead.getEmail() != null) {
            // 🔥 Regra: evitar duplicidade de email
            if (!lead.getEmail().equals(dto.getEmail()) &&
                    repository.existsByEmail(dto.getEmail())) {
                throw new BusinessException("Email já cadastrado");
            }
        }
        if (!lead.getTelefone().equals(dto.getTelefone()) &&
                repository.existsByTelefone(dto.getTelefone())) {
            throw new BusinessException("Telefone já cadastrado");
        }
        Lead updated = repository.save(LeadMapper.updateEntity(lead, dto));

        return LeadMapper.toDTO(updated);
    }

    public LeadResponseDTO patchLeadStatus(UUID id, StatusLead status){
        Lead lead = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Lead não encontrado em patch Status"));
        lead.setStatus(status);
        Lead patched = repository.save(lead);
        return LeadMapper.toDTO(patched);
    }

    public LeadResponseDTO patchLeadCorretor(UUID leadId, UUID userId){
        User userLogged = SecurityUtils.getCurrentUser();
        boolean isGerente = userLogged.getRole().name().equals("GERENTE");
        if(!isGerente){
            throw new BusinessException("Você não tem permissão para alterar essa informação");
        }
        Lead lead = repository.findById(leadId)
                .orElseThrow(() -> new BusinessException("Lead não encontrado em patch corretor"));
        User user = userRepository.findById(userId).orElseThrow(() -> new BusinessException("user não encontrado em patch corretor"));

        lead.setUser(user);
        Lead patched = repository.save(lead);
        return LeadMapper.toDTO(patched);
    }

    public void delete(UUID id) {
        User user = SecurityUtils.getCurrentUser();

        if (!user.getRole().name().equals("GERENTE")) {
            throw new BusinessException("Apenas gerentes podem deletar leads");
        }

        Lead lead = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Lead não encontrado"));


        repository.delete(lead);
    }


    public LeadDashboardDTO getDashboard(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User não encontrado no dashboard"));;

        boolean isGerente = user.getRole().name().equals("GERENTE");
        // 👇 null = todos os leads
        List<StatusCount> result = repository.countByStatus(isGerente ? null : user);

        Map<String, Long> porStatus = new HashMap<>();
        long total = 0;

        for (StatusCount row : result) {
            porStatus.put(row.getStatus().name(), row.getCount());
            total += row.getCount();
        }

        return new LeadDashboardDTO(total, porStatus);
    }

    public LeadDashboardDTO getDashboardByUser(UUID userId) {

        User user = SecurityUtils.getCurrentUser();

        boolean isGerente = user.getRole().name().equals("GERENTE");

        // 👇 null = todos os leads
        List<StatusCount> result = repository.countByStatusByUser(userId);

        Map<String, Long> porStatus = new HashMap<>();
        long total = 0;

        for (StatusCount row : result) {
            porStatus.put(row.getStatus().name(), row.getCount());
            total += row.getCount();
        }

        return new LeadDashboardDTO(total, porStatus);
    }


}
