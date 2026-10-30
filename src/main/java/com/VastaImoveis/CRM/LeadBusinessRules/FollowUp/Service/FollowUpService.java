package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Service;


import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUp;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUpStatus;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto.*;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Mapper.FollowUpMapper;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Repository.FollowUpRepository;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Repository.LeadRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;

    public FollowUpService(
            FollowUpRepository followUpRepository,
            LeadRepository leadRepository,
            UserRepository userRepository
    ) {
        this.followUpRepository = followUpRepository;
        this.leadRepository = leadRepository;
        this.userRepository = userRepository;
    }

    private void exigirFollowUpAtual(FollowUp followUp) {
        if (!followUp.isAtual()) {
            throw new IllegalStateException(
                    "Este Follow-up não é mais o registro atual do Lead"
            );
        }

        UUID leadId = followUp.getLead().getId();

        FollowUp atual = followUpRepository
                .findByLead_IdAndAtualTrue(leadId)
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Follow-up atual não encontrado"
                        ));

        if (!atual.getId().equals(followUp.getId())) {
            throw new IllegalStateException(
                    "Este Follow-up não é o registro atual do Lead"
            );
        }
    }

    private UUID buscarUserIdAutenticado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("Usuário não autenticado");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Usuário autenticado não encontrado"
                        )
                )
                .getId();
    }

    // Cria o primeiro contato do ciclo: 1/N.
    @Transactional
    public FollowUpResponseDTO criarCiclo(
            UUID leadId,
            CreateFollowUpRequest request
    ) {
        Lead lead = leadRepository.findById(leadId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Lead não encontrado"));

        if (followUpRepository.existsByLead_IdAndAtualTrue(leadId)) {
            throw new IllegalStateException(
                    "Este Lead já possui um ciclo de Follow-ups ativo"
            );
        }
        FollowUp followUp = FollowUpMapper.toEntityCreate(request, lead);
        return FollowUpMapper.toResponse(followUpRepository.save(followUp));
    }


    // Cria automaticamente o primeiro Follow-up ao cadastrar um Lead.
    @Transactional
    public FollowUpResponseDTO createInitialFollowUp(Lead lead) {

        if (lead == null || lead.getId() == null) {
            throw new IllegalArgumentException(
                    "O Lead precisa estar salvo antes de criar o Follow-up"
            );
        }

        UUID leadId = lead.getId();

        // Impede a criação de outro Follow-up ativo.
        if (followUpRepository.existsByLead_IdAndAtualTrue(leadId)) {
            throw new IllegalStateException(
                    "Este Lead já possui um ciclo de Follow-ups ativo"
            );
        }

        // Reutiliza as regras de criação existentes no Mapper.
        CreateFollowUpRequest request = new CreateFollowUpRequest(null);

        FollowUp followUp = FollowUpMapper.toEntityCreate(
                request,
                lead
        );

        FollowUp saved = followUpRepository.save(followUp);

        return FollowUpMapper.toResponse(saved);
    }


    @Transactional(readOnly = true)
    public Page<FollowUpResponseDTO> findAll(Pageable pageable) {

        return followUpRepository
                .findAll(pageable)
                .map(FollowUpMapper::toResponse);
    }

    // Consulta o histórico completo do Lead.
    @Transactional(readOnly = true)
    public Page<FollowUpResponseDTO> listarPorLead(UUID leadId, Pageable page) {
        if (!leadRepository.existsById(leadId)) {
            throw new EntityNotFoundException("Lead não encontrado");
        }

        return followUpRepository
                .findByLead_IdOrderByCreatedAtDesc(leadId, page)
                .map(FollowUpMapper::toResponse);
    }

    // Consulta um registro específico.
    @Transactional(readOnly = true)
    public FollowUpResponseDTO buscarPorId(UUID id) {
        return FollowUpMapper.toResponse(buscarEntidade(id));
    }

    // Lista todos os Follow-ups atuais.
// Utilizado por administradores e gerentes.
    @Transactional(readOnly = true)
    public Page<FollowUpResponseDTO> findCurrent(Pageable pageable) {
        return followUpRepository
                .findByAtualTrue(pageable)
                .map(FollowUpMapper::toResponse);
    }


    // Retorna a quantidade de Follow-ups atuais agrupados por status.
// Utilizado por administradores e gerentes.
    @Transactional(readOnly = true)
    public List<FollowUpStatusCountDTO> countCurrentByStatus() {
        return followUpRepository.countCurrentByStatus();
    }

    // Lista somente os Follow-ups atuais do corretor autenticado.
    @Transactional(readOnly = true)
    public Page<FollowUpResponseDTO> findMyCurrent(
            Authentication user,
            Pageable pageable
    ) {
        UUID userId = buscarUserIdAutenticado(user);

        return followUpRepository
                .findByAtualTrueAndLead_User_Id(userId, pageable)
                .map(FollowUpMapper::toResponse);
    }


    @Transactional(readOnly = true)
    public Page<FollowUpResponseDTO> findCurrentByUser(
            UUID userId,
            Pageable pageable
    ) {

        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(
                    "Usuário não encontrado"
            );
        }

        return followUpRepository
                .findByAtualTrueAndLead_User_Id(userId, pageable)
                .map(FollowUpMapper::toResponse);
    }


    // Retorna a quantidade de Follow-ups atuais por status
// somente para o corretor autenticado.
    @Transactional(readOnly = true)
    public List<FollowUpStatusCountDTO> countMyCurrentByStatus(
            Authentication user
    ) {

        UUID userId = buscarUserIdAutenticado(user);

        LocalDateTime inicioDeHoje = LocalDate.now().atStartOfDay();

        // Busca a contagem original agrupada por status
        List<FollowUpStatusCountDTO> counts =
                followUpRepository.countCurrentByStatusAndUser(userId);

        // Conta os pendentes que estão atrasados
        Long pendentesAtrasados = followUpRepository.countAtrasadosByUser(
                userId,
                FollowUpStatus.PENDENTE,
                inicioDeHoje
        );

        // Conta os reagendados que estão atrasados
        Long reagendadosAtrasados = followUpRepository.countAtrasadosByUser(
                userId,
                FollowUpStatus.REAGENDADO,
                inicioDeHoje
        );

        // Reconstrói as quantidades com os status efetivos
        return calcularStatusEfetivos(
                counts,
                pendentesAtrasados,
                reagendadosAtrasados
        );
    }

    // Conclui o contato e salva o atraso histórico.
    @Transactional
    public FollowUpResponseDTO concluir(UUID id) {
        FollowUp followUp = buscarEntidade(id);

        exigirFollowUpAtual(followUp);

        exigirStatusAcionavel(
                followUp);

        LocalDateTime agora = LocalDateTime.now();

        followUp.setStatus(FollowUpStatus.CONCLUIDO);
        followUp.setUltimoContato(agora);
        followUp.setConcluidoEm(agora);
        followUp.setEncerradoEm(agora);

        followUp.setDiasEmAtraso(
                FollowUpMapper.calcularDiasAtraso(
                        followUp.getProximoContato(),
                        agora
                )
        );

        return FollowUpMapper.toResponse(
                followUpRepository.save(followUp)
        );
    }

    // Reagenda sem perder o registro anterior.
    @Transactional
    public FollowUpResponseDTO reagendar(
            UUID id,
            RescheduleFollowUpRequestDTO request
    ) {
        FollowUp anterior = buscarEntidade(id);

        exigirFollowUpAtual(anterior);

        exigirStatusAcionavel(
                anterior);

        // Valida a data e prepara o novo registro antes das alterações.
        FollowUp novo = FollowUpMapper.reagendarMapper(
                anterior,
                request
        );

        LocalDateTime agora = LocalDateTime.now();

        anterior.setStatus(FollowUpStatus.REAGENDADO);
        anterior.setEncerradoEm(agora);
        anterior.setAtual(false);
        anterior.setDiasEmAtraso(
                FollowUpMapper.calcularDiasAtraso(
                        anterior.getProximoContato(),
                        agora
                )
        );

        // Libera a posição de registro atual antes da inserção seguinte.
        followUpRepository.saveAndFlush(anterior);

        return FollowUpMapper.toResponse(
                followUpRepository.save(novo)
        );
    }

    // Cria o próximo número somente após concluir o atual.
    @Transactional
    public FollowUpResponseDTO agendarProximo(
            UUID id,
            NextFollowUpRequestDTO request
    ) {

        FollowUp anterior = buscarEntidade(id);

        exigirFollowUpAtual(anterior);

        exigirStatusAcionavel(
                anterior);

        if (anterior.getFollowUpAtual()
                >= anterior.getFollowUpTotal()) {

            throw new IllegalStateException(
                    "O ciclo de Follow-ups já foi concluído"
            );
        }

        // Conclui o contato anterior.
        concluir(anterior.getId());

        // Libera a posição de Follow-up atual.
        anterior.setAtual(false);

        // Garante que o UPDATE seja enviado antes do INSERT.
        followUpRepository.saveAndFlush(anterior);

        // Cria a próxima etapa.
        FollowUp proximo =
                FollowUpMapper.novoAgendamentoMapper(
                        anterior,
                        request
                );

        FollowUp salvo =
                followUpRepository.save(proximo);

        return FollowUpMapper.toResponse(salvo);
    }

    private FollowUp buscarEntidade(UUID id) {
        return followUpRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Follow-up não encontrado"
                        ));
    }

    private void exigirStatusAcionavel(FollowUp followUp) {

        if (
                followUp.getStatus() != FollowUpStatus.PENDENTE &&
                        followUp.getStatus() != FollowUpStatus.REAGENDADO
        ) {
            throw new IllegalStateException(
                    "Somente Follow-ups pendentes ou reagendados podem realizar esta operação"
            );
        }
    }

    // Follow-ups do usuário autenticado
    @Transactional(readOnly = true)
    public Page<FollowUpResponseDTO> filterMyCurrent(
            Authentication authentication,
            String search,
            FollowUpStatus status,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {

        UUID userId = buscarUserIdAutenticado(authentication);

        return filterCurrent(
                search,
                status,
                userId,
                startDate,
                endDate,
                pageable
        );
    }


    // Follow-ups gerais (administrador)
    @Transactional(readOnly = true)
    public Page<FollowUpResponseDTO> filterAllCurrent(
            String search,
            FollowUpStatus status,
            UUID userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {

        return filterCurrent(
                search,
                status,
                userId,
                startDate,
                endDate,
                pageable
        );
    }


    // Lógica compartilhada
    private Page<FollowUpResponseDTO> filterCurrent(
            String search,
            FollowUpStatus status,
            UUID userId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable
    ) {

        if (
                startDate != null
                        && endDate != null
                        && startDate.isAfter(endDate)
        ) {
            throw new IllegalArgumentException(
                    "A data inicial não pode ser posterior à data final"
            );
        }

        LocalDateTime inicioDeHoje = LocalDate.now().atStartOfDay();

        LocalDateTime inicio = startDate != null
                ? startDate.atStartOfDay()
                : LocalDate.of(1900, 1, 1).atStartOfDay();

        LocalDateTime fim = endDate != null
                ? endDate.plusDays(1).atStartOfDay()
                : LocalDate.of(9999, 12, 31).atStartOfDay();

        Page<FollowUp> resultado = followUpRepository.filter(
                search == null ? "" : search.trim(),
                status,
                userId,
                inicio,
                fim,
                FollowUpStatus.PENDENTE,
                FollowUpStatus.REAGENDADO,
                FollowUpStatus.ATRASADO,
                inicioDeHoje,
                pageable
        );

        return resultado.map(FollowUpMapper::toResponse);
    }


    @Transactional(readOnly = true)
    public FollowUpSummaryDTO getMySummary(Authentication authentication) {

        UUID userId = buscarUserIdAutenticado(authentication);

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioDeHoje = hoje.atStartOfDay();
        LocalDateTime inicioDeAmanha = hoje.plusDays(1).atStartOfDay();

        List<FollowUpStatusCountDTO> counts =
                countMyCurrentByStatus(authentication);

        Long todayCount = followUpRepository.countTodayByUser(
                userId,
                FollowUpStatus.PENDENTE,
                inicioDeHoje,
                inicioDeAmanha
        );

        return FollowUpMapper.toSummaryDTO(counts, todayCount);
    }

    @Transactional
    public FollowUpResponseDTO cancelar(UUID id) {

        FollowUp followUp = buscarEntidade(id);

        exigirFollowUpAtual(followUp);

        exigirStatusAcionavel(
                followUp);

        LocalDateTime agora = LocalDateTime.now();

        followUp.setStatus(FollowUpStatus.CANCELADO);

        followUp.setAtual(false);

        followUp.setEncerradoEm(agora);

        followUp.setDiasEmAtraso(
                FollowUpMapper.calcularDiasAtraso(
                        followUp.getProximoContato(),
                        agora
                )
        );

        return FollowUpMapper.toResponse(
                followUpRepository.save(followUp)
        );
    }

    private List<FollowUpStatusCountDTO> calcularStatusEfetivos(
            List<FollowUpStatusCountDTO> counts,
            Long pendentesAtrasados,
            Long reagendadosAtrasados
    ) {
        List<FollowUpStatusCountDTO> resultado = new ArrayList<>();

        for (FollowUpStatusCountDTO count : counts) {

            long quantidade = count.quantidade();

            if (count.status() == FollowUpStatus.PENDENTE) {
                quantidade -= pendentesAtrasados;
            }

            if (count.status() == FollowUpStatus.REAGENDADO) {
                quantidade -= reagendadosAtrasados;
            }

            if (count.status() != FollowUpStatus.ATRASADO) {
                resultado.add(
                        new FollowUpStatusCountDTO(
                                count.status(),
                                quantidade
                        )
                );
            }
        }

        resultado.add(
                new FollowUpStatusCountDTO(
                        FollowUpStatus.ATRASADO,
                        pendentesAtrasados + reagendadosAtrasados
                )
        );

        return resultado;
    }

    @Transactional(readOnly = true)
    public FollowUpSummaryDTO getAllSummary() {

        LocalDate hoje = LocalDate.now();

        LocalDateTime inicioDeHoje = hoje.atStartOfDay();
        LocalDateTime inicioDeAmanha = hoje.plusDays(1).atStartOfDay();

        List<FollowUpStatusCountDTO> counts =
                followUpRepository.countCurrentByStatus();

        Long pendentesAtrasados = followUpRepository.countAllAtrasados(
                FollowUpStatus.PENDENTE,
                inicioDeHoje
        );

        Long reagendadosAtrasados = followUpRepository.countAllAtrasados(
                FollowUpStatus.REAGENDADO,
                inicioDeHoje
        );

        List<FollowUpStatusCountDTO> statusEfetivos =
                calcularStatusEfetivos(
                        counts,
                        pendentesAtrasados,
                        reagendadosAtrasados
                );
        Long todayCount = followUpRepository.countAllToday(
                FollowUpStatus.PENDENTE,
                inicioDeHoje,
                inicioDeAmanha
        );

        return FollowUpMapper.toSummaryDTO(
                statusEfetivos,
                todayCount
        );
    }
}
