package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Mapper;

import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUp;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUpStatus;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto.*;
import com.VastaImoveis.CRM.LeadBusinessRules.Lead.Entity.Domain.Lead;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class FollowUpMapper {

    private static final Map<Integer, Integer> DIAS_POR_ATENDIMENTO = Map.of(
            1, 2,
            2, 5,
            3, 10,
            4, 20,
            5, 30,
            6, 40,
            7, 50,
            8, 60,
            9, 70,
            10, 80
    );

    private static final int TOTAL_ATENDIMENTOS = 10;

    static private LeadSummary fromLead(Lead lead) {
        return new LeadSummary(
                lead.getId(),
                lead.getNome(),
                lead.getTelefone(),
                lead.getUser().getId(),
                lead.getStatus()
        );
    }

    static public FollowUpStatus calcularStatusEfetivo(FollowUp followUp) {

        if (
                followUp.getStatus() == FollowUpStatus.PENDENTE
                        && followUp.getProximoContato() != null
                        && followUp.getProximoContato().toLocalDate()
                        .isBefore(LocalDate.now())
        ) {
            return FollowUpStatus.ATRASADO;
        }

        return followUp.getStatus();
    }

    static public int calcularDiasAtraso(
            LocalDateTime previsto,
            LocalDateTime encerramento
    ) {
        if (previsto == null || encerramento == null) {
            return 0;
        }

        long dias = ChronoUnit.DAYS.between(
                previsto.toLocalDate(),
                encerramento.toLocalDate()
        );

        return (int) Math.max(0, dias);
    }

    static private void validarDataFutura(LocalDateTime data) {
        if (data == null || !data.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "A data do próximo contato deve ser futura"
            );
        }
    }

    static public FollowUpResponseDTO toResponse(FollowUp f) {
        int diasEmAtraso;

        if (f.getStatus() == FollowUpStatus.PENDENTE) {
            diasEmAtraso = calcularDiasAtraso(
                    f.getProximoContato(),
                    LocalDate.now().atStartOfDay()
            );
        } else {
            diasEmAtraso = f.getDiasEmAtraso() == null
                    ? 0
                    : f.getDiasEmAtraso();
        }

        LeadSummary leadSummary = fromLead(f.getLead());

        return new FollowUpResponseDTO(
                f.getId(),
                leadSummary,
                f.getFollowUpAtual(),
                f.getFollowUpTotal(),
                f.getProximoContato(),
                f.getUltimoContato(),
                f.getConcluidoEm(),
                f.getEncerradoEm(),
                f.getCreatedAt(),
                diasEmAtraso,
                calcularStatusEfetivo(f),
                f.getObservacao()
        );
    }


    public static FollowUp toEntityCreate(
            CreateFollowUpRequest request,
            Lead lead
    ) {
        FollowUp followUp = new FollowUp();

        int primeiroAtendimento = 1;


        Integer dias = DIAS_POR_ATENDIMENTO.get(primeiroAtendimento);

        if (dias == null) {
            throw new IllegalStateException(
                    "Não existe prazo configurado para o atendimento "
                            + primeiroAtendimento
            );
        }

        LocalDateTime criadoEm = LocalDateTime.now();

        followUp.setLead(lead);
        followUp.setStatus(FollowUpStatus.PENDENTE);
        followUp.setFollowUpAtual(primeiroAtendimento);
        followUp.setFollowUpTotal(TOTAL_ATENDIMENTOS);
        followUp.setProximoContato(criadoEm.plusDays(dias));
        followUp.setObservacao(request.observacao());
        followUp.setAtual(true);

        return followUp;
    }

    static public FollowUp reagendarMapper(FollowUp anterior, RescheduleFollowUpRequestDTO request) {
        validarDataFutura(request.proximoContato());

        FollowUp novo = new FollowUp();

        novo.setLead(anterior.getLead());
        novo.setFollowUpAtual(anterior.getFollowUpAtual());
        novo.setFollowUpTotal(anterior.getFollowUpTotal());
        novo.setStatus(FollowUpStatus.PENDENTE);
        novo.setAtual(true);
        novo.setProximoContato(request.proximoContato());
        novo.setObservacao(request.observacao());

        return novo;
    }

    static public FollowUp novoAgendamentoMapper(
            FollowUp anterior,
            NextFollowUpRequestDTO request
    ) {
        if (anterior.getStatus() != FollowUpStatus.CONCLUIDO) {
            throw new IllegalStateException(
                    "O atendimento atual precisa estar concluído."
            );
        }

        int proximoAtendimento = anterior.getFollowUpAtual() + 1;

        if (proximoAtendimento > anterior.getFollowUpTotal()) {
            throw new IllegalStateException(
                    "O ciclo de follow-ups já foi concluído."
            );
        }

        Integer dias = DIAS_POR_ATENDIMENTO.get(proximoAtendimento);

        if (dias == null) {
            throw new IllegalStateException(
                    "Não existe prazo configurado para o atendimento "
                            + proximoAtendimento
            );
        }

        LocalDateTime criadoEm = LocalDateTime.now();

        FollowUp proximo = new FollowUp();

        proximo.setLead(anterior.getLead());
        proximo.setFollowUpAtual(proximoAtendimento);
        proximo.setFollowUpTotal(anterior.getFollowUpTotal());
        proximo.setAtual(true);
        proximo.setStatus(FollowUpStatus.PENDENTE);
        proximo.setProximoContato(criadoEm.plusDays(dias));
        proximo.setObservacao(request.observacao());

        return proximo;
    }

    public static FollowUpSummaryDTO toSummaryDTO(
            List<FollowUpStatusCountDTO> counts,
            Long todayCount
    ) {
        return new FollowUpSummaryDTO(
                counts,
                todayCount
        );
    }
}
