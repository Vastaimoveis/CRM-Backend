package com.VastaImoveis.CRM.LeadNotes.Controller;

import com.VastaImoveis.CRM.LeadNotes.Entity.dto.LeadNoteRequestDTO;
import com.VastaImoveis.CRM.LeadNotes.Entity.dto.LeadNoteResponseDTO;
import com.VastaImoveis.CRM.LeadNotes.service.LeadNoteService;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/leadNotes")
public class LeadNoteController {
    private final LeadNoteService service;

    public LeadNoteController(LeadNoteService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('GERENTE', 'CORRETOR')")
    @PostMapping
    public ResponseEntity<ApiResponse<LeadNoteResponseDTO>> create(
            @RequestBody @Valid LeadNoteRequestDTO dto) {
        LeadNoteResponseDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                    new ApiResponse<>(true, created, "Anotação criada com sucesso")
        );
    }

    @GetMapping("/{leadId}")
    public ResponseEntity<ApiResponse<Page<LeadNoteResponseDTO>>> findByLeadId(
            @PathVariable UUID leadId,
            @PageableDefault(size = 30) Pageable pageable
    ) {
        Page<LeadNoteResponseDTO> page = service.findByLeadId(leadId, pageable);
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        new ApiResponse<>(true, page, "Anotações listadas com sucesso")
                );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteById(
            @PathVariable UUID id
    ){
        service.delete(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, null, "Nota deletada com sucesso")
        );
    }

}


