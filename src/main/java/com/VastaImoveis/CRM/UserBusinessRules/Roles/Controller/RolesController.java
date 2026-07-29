package com.VastaImoveis.CRM.UserBusinessRules.Roles.Controller;

import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto.RoleRequestDto;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto.RoleResponseDto;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Service.RoleService;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RolesController {
    private final RoleService service;

    public RolesController(RoleService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('GERENTE')")
    @PostMapping
    public ResponseEntity<ApiResponse<RoleResponseDto>> createRole(RoleRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        true,
                        service.create(dto),
                        "Cargo criado com sucesso"
                )
        );
    }

    @PreAuthorize("hasAnyRole('GERENTE')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> findAll(){
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        true,
                        service.findAll(),
                        "Cargo criado com sucesso"
                )
        );
    }

    @PreAuthorize("hasAnyRole('GERENTE')")
    @DeleteMapping
    public ResponseEntity<ApiResponse<>>
}
