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
import java.util.UUID;

@RestController
@RequestMapping("/role")
public class RolesController {
    private final RoleService service;

    public RolesController(RoleService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('USER_CREATE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> createRole(RoleRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        true,
                        service.create(dto),
                        "Cargo criado com sucesso"
                )
        );
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('USER_CREATE')")
    public ResponseEntity<ApiResponse<List<RoleResponseDto>>> findAll() {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        true,
                        service.findAll(),
                        "Cargos buscados com sucesso"
                )
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('USER_CREATE')")
    public ResponseEntity<ApiResponse<RoleResponseDto>> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(
                new ApiResponse<>(
                        true,
                        service.findById(id),
                        "Role encontrada com sucesso"
                )
        );
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('USER_CREATE')")
    public ResponseEntity<ApiResponse> Delete(UUID id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(
                        true,
                        null,
                        "Cargo deletado com scuesso"
                )
        );
    }
}
