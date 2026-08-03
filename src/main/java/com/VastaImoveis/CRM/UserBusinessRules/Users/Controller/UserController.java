package com.VastaImoveis.CRM.UserBusinessRules.Users.Controller;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.updateUserRoleDto;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.UserRequestDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.UserResponseDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Criar usuário")
    @PostMapping
    @PreAuthorize("hasAuthority('USER_CREATE')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> create(@RequestBody @Valid UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, service.create(dto), "User criado com sucesso"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_EDIT')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> update(
            @PathVariable UUID id,
            @RequestBody @Valid UserRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, service.update(id, dto), "User alterado com sucesso"));
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasAuthority('USER_CHANGE_ROLE')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> patchRole(
            @PathVariable UUID id,
            @RequestBody @Valid updateUserRoleDto roleId) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, service.patchRole(id, roleId), "Role do user atualizada com sucesso")
            );
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<ApiResponse<UserResponseDTO>> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, service.findById(id), "User encontrado com sucesso"));
    }

    @GetMapping("/regiao/{Regiao}")
    @PreAuthorize("hasAuthority('USER_VIEW')")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> findByRegiao(@PathVariable("Regiao") RegiaoUsers regiaoUsers, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        new ApiResponse<>(true, service.listUserByRegiao(regiaoUsers, pageable), "Users listados por região com sucesso"));
    }

    @GetMapping
    @PreAuthorize("USER_VIEW")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> findAll(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        new ApiResponse<>(true, service.findAll(pageable), "Users listados com sucesso"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER_DELETE')")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") UUID userId) {
        service.delete(userId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, null, "User deletado com sucesso")
        );
    }


}