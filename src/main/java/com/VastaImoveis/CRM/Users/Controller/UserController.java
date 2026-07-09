package com.VastaImoveis.CRM.Users.Controller;

import com.VastaImoveis.CRM.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;
import com.VastaImoveis.CRM.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.Users.Entity.dto.UserRequestDTO;
import com.VastaImoveis.CRM.Users.Entity.dto.UserResponseDTO;
import com.VastaImoveis.CRM.Users.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @Operation(summary = "Criar usuário")
    @PreAuthorize("hasAnyRole('GERENTE')")
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDTO>> create(@RequestBody @Valid UserRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, service.create(dto), "User criado com sucesso"));
    }

    @PreAuthorize("hasAnyRole('GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> update(
            @PathVariable UUID id,
            @RequestBody @Valid UserRequestDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, service.update(id, dto), "User alterado com sucesso"));
    }

    @PreAuthorize("hasAnyRole('GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDTO>> findById(@PathVariable UUID id){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, service.findById(id), "User encontrado com sucesso"));
    }

    @PreAuthorize("hasAnyRole('GERENTE')")
    @GetMapping("/regiao/{Regiao}")
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> findByRegiao(@PathVariable("Regiao") RegiaoUsers regiaoUsers, Pageable pageable){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        new ApiResponse<>(true, service.listUserByRegiao(regiaoUsers, pageable),"Users listados por região com sucesso"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponseDTO>>> findAll(Pageable pageable){
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(
                        new ApiResponse<>(true, service.findAll(pageable), "Users listados com sucesso"));
    }

    @PreAuthorize("hasAnyRole('GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") UUID userId){
        service.delete(userId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                new ApiResponse<>(true, null, "User deletado com sucesso")
        );
    }
}