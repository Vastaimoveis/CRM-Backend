package com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Controller;

import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.Domain.FollowUpStatus;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Entity.dto.*;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Mapper.FollowUpMapper;
import com.VastaImoveis.CRM.LeadBusinessRules.FollowUp.Service.FollowUpService;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/follow-ups")
public class FollowUpController {

    private final FollowUpService service;

    public FollowUpController(FollowUpService service) {
        this.service = service;
    }

    /**
     * Lista os Follow-ups de um Lead com paginação.
     */
    @GetMapping("/lead/{leadId}")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    public ResponseEntity<ApiResponse<Page<FollowUpResponseDTO>>> findByLead(
            @PathVariable UUID leadId,
            @PageableDefault(size = 10) Pageable pageable
    ) {

        Page<FollowUpResponseDTO> page =
                service.listarPorLead(leadId, pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                page,
                                "Follow-ups listados com sucesso"
                        )
                );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    public ResponseEntity<ApiResponse<Page<FollowUpResponseDTO>>> findAll(
            @PageableDefault(size = 10) Pageable pageable
    ) {

        Page<FollowUpResponseDTO> page =
                service.findAll(pageable);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                page,
                                "Follow-ups listados com sucesso"
                        )
                );
    }

    /**
     * Busca um Follow-up específico.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    public ResponseEntity<ApiResponse<FollowUpResponseDTO>> findById(
            @PathVariable UUID id
    ) {

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                service.buscarPorId(id),
                                "Follow-up buscado com sucesso"
                        )
                );
    }

    /**
     * Cria o primeiro Follow-up/ciclo de um Lead.
     */
    @PostMapping("/lead/{leadId}")
    @PreAuthorize("hasAuthority('FOLLOWUP_CREATE')")
    public ResponseEntity<ApiResponse<FollowUpResponseDTO>> create(
            @PathVariable UUID leadId,
            @RequestBody @Valid CreateFollowUpRequest request
    ) {

        FollowUpResponseDTO created =
                service.criarCiclo(leadId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                created,
                                "Ciclo de Follow-up criado com sucesso"
                        )
                );
    }


    /**
     * Conclui o Follow-up atual.
     */
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('FOLLOWUP_COMPLETE')")
    public ResponseEntity<ApiResponse<FollowUpResponseDTO>> complete(
            @PathVariable UUID id
    ) {

        FollowUpResponseDTO completed =
                service.concluir(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(
                        new ApiResponse<>(
                                true,
                                completed,
                                "Follow-up concluído com sucesso"
                        )
                );
    }


    /**
     * Reagenda o Follow-up atual.
     * <p>
     * Regra do Modelo B:
     * o registro anterior é preservado no histórico
     * e um novo registro é criado.
     */
    @PostMapping("/{id}/reschedule")
    @PreAuthorize("hasAuthority('FOLLOWUP_RESCHEDULE')")
    public ResponseEntity<ApiResponse<FollowUpResponseDTO>> reschedule(
            @PathVariable UUID id,
            @RequestBody @Valid RescheduleFollowUpRequestDTO request
    ) {

        FollowUpResponseDTO rescheduled =
                service.reagendar(id, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                rescheduled,
                                "Follow-up reagendado com sucesso"
                        )
                );
    }

    @GetMapping("/current")
    @PreAuthorize("hasAnyAuthority('FOLLOWUP_VIEW_ALL')")
    public ResponseEntity<ApiResponse<Page<FollowUpResponseDTO>>> findCurrent(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        service.findCurrent(pageable),
                        "Follow-ups atuais listados com sucesso"
                )
        );
    }

    @GetMapping("/current/count-by-status")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW_ALL')")
    public ResponseEntity<ApiResponse<FollowUpSummaryDTO>> countCurrentByStatus() {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        service.getAllSummary(),
                        "Resumo geral dos Follow-ups consultado com sucesso"
                )
        );
    }

    @GetMapping("/current/me")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    public ResponseEntity<ApiResponse<Page<FollowUpResponseDTO>>> findMyCurrent(
            Authentication authentication,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        service.findMyCurrent(authentication, pageable),
                        "Seus Follow-ups atuais foram listados com sucesso"
                )
        );
    }

    @GetMapping("/current/user/{userId}")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW_USERS')")
    public ResponseEntity<ApiResponse<Page<FollowUpResponseDTO>>> findCurrentByUser(
            @PathVariable UUID userId,
            @PageableDefault(size = 10) Pageable pageable
    ) {

        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(true,
                        service.findCurrentByUser(userId, pageable),
                        "Follow-ups do usuário encontrados com sucesso"
                )
        );
    }

    @GetMapping("/current/me/count-by-status")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    public ResponseEntity<ApiResponse<FollowUpSummaryDTO>> countMyCurrentByStatus(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        service.getMySummary(authentication),
                        "Resumo dos seus Follow-ups consultado com sucesso"
                )
        );
    }

    /**
     * Cria o próximo Follow-up do ciclo.
     * <p>
     * Exemplo:
     * <p>
     * 1/6 → 2/6
     * 2/6 → 3/6
     * ...
     * 5/6 → 6/6
     */
    @PostMapping("/{id}/next")
    @PreAuthorize("hasAuthority('FOLLOWUP_CREATE')")
    public ResponseEntity<ApiResponse<FollowUpResponseDTO>> next(
            @PathVariable UUID id,
            @RequestBody @Valid NextFollowUpRequestDTO request
    ) {

        FollowUpResponseDTO next =
                service.agendarProximo(id, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(
                                true,
                                next,
                                "Próximo Follow-up agendado com sucesso"
                        )
                );
    }

    @GetMapping("/filter/me")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW')")
    public ResponseEntity<ApiResponse<Page<FollowUpResponseDTO>>> filterMyCurrent(
            Authentication authentication,

            @RequestParam(required = false) String search,

            @RequestParam(required = false)
            FollowUpStatus status,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @PageableDefault(size = 10)
            Pageable pageable
    ) {

        Page<FollowUpResponseDTO> resultado =
                service.filterMyCurrent(
                        authentication,
                        search,
                        status,
                        startDate,
                        endDate,
                        pageable
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        resultado,
                        "Seus Follow-ups foram filtrados com sucesso"
                )
        );
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('FOLLOWUP_VIEW_ALL')")
    public ResponseEntity<ApiResponse<Page<FollowUpResponseDTO>>> filterAllCurrent(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            FollowUpStatus status,

            @RequestParam(required = false)
            UUID userId,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @PageableDefault(size = 10)
            Pageable pageable
    ) {

        Page<FollowUpResponseDTO> resultado =
                service.filterAllCurrent(
                        search,
                        status,
                        userId,
                        startDate,
                        endDate,
                        pageable
                );

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        resultado,
                        "Follow-ups gerais filtrados com sucesso"
                )
        );
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('FOLLOWUP_CANCEL')")
    public ResponseEntity<ApiResponse<FollowUpResponseDTO>> cancel(
            @PathVariable UUID id
    ) {

        FollowUpResponseDTO cancelled = service.cancelar(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        cancelled,
                        "Ciclo de Follow-ups cancelado com sucesso"
                )
        );
    }
}