package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Controller;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto.PermissionRequestDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto.PermissionResponseDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Service.PermissionsService;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/permission")
public class PermissionsController {

    private final PermissionsService service;

    public PermissionsController(PermissionsService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PermissionResponseDTO>> listById(UUID id) {
        return ResponseEntity.status(HttpStatus.FOUND).body(
                new ApiResponse<>(
                        true,
                        service.getById(id),
                        "Permissão encontrada com sucesso"
                )
        );
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<PermissionResponseDTO>>> listAll() {
        return ResponseEntity.status(HttpStatus.FOUND).body(
                new ApiResponse<>(
                        true,
                        service.findAll(),
                        "Permissão encontrada com sucesso"
                )
        );
    }


    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping
    public ResponseEntity<ApiResponse> deletePermission(UUID id) {
        service.delete(id);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(

                new ApiResponse<>(
                        true,
                        null,
                        "Permissão deletada com sucesso"
                )
        );
    }
}
