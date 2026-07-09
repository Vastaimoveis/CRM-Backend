package com.VastaImoveis.CRM.Lead.Controller;

import com.VastaImoveis.CRM.Lead.Entity.dto.LeadPublicRequestDTO;
import com.VastaImoveis.CRM.Lead.Entity.dto.LeadResponseDTO;
import com.VastaImoveis.CRM.Lead.Service.PublicLeadService;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/leads")
public class PublicLeadController {
    private final PublicLeadService service;

    public PublicLeadController(PublicLeadService service) {
        this.service = service;
    }



    // Public
    @PostMapping()
    public ResponseEntity<ApiResponse<LeadResponseDTO>> publicCreate(
            @RequestBody @Valid LeadPublicRequestDTO dto) {
        LeadResponseDTO created = service.publicCreate(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        new ApiResponse<>(true, created, "Lead criado com sucesso")
                );
    }
}
