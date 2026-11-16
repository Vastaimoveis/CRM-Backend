
package com.VastaImoveis.CRM.ScriptBusinessRules.Script.Controller;

import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Domain.ScriptCategories;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptCreateDTO;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptResponseDTO;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptUpdateDTO;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Service.ScriptService;
import com.VastaImoveis.CRM.shared.utils.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/scripts")
public class ScriptController {

    private final ScriptService service;

    public ScriptController(ScriptService service) {
        this.service = service;
    }

    // Criar script
    @PostMapping
    @PreAuthorize("hasAuthority('SCRIPT_CREATE')")
    public ResponseEntity<ApiResponse<ScriptResponseDTO>> create(
            @RequestBody @Valid ScriptCreateDTO dto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>(
                        true,
                        service.create(dto),
                        "Script criado com sucesso"
                )
        );
    }

    // Listar scripts do usuário autenticado
    @GetMapping
    @PreAuthorize("hasAuthority('SCRIPT_VIEW')")
    public ResponseEntity<ApiResponse<Page<ScriptResponseDTO>>> findAll(
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        service.findAll(pageable),
                        "Scripts listados com sucesso"
                )
        );
    }

    // Buscar scripts por título ou mensagem
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCRIPT_VIEW')")
    public ResponseEntity<ApiResponse<Page<ScriptResponseDTO>>> search(
            @RequestParam String query,
            Pageable pageable
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        service.search(query, pageable),
                        "Scripts encontrados com sucesso"
                )
        );
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('SCRIPT_VIEW')")
    public ResponseEntity<ApiResponse<Page<ScriptResponseDTO>>> filter(
            @RequestParam(required = false) ScriptCategories category,
            Pageable pageable
    ) {
        Page<ScriptResponseDTO> scripts;

        if (category == null) {
            scripts = service.findAll(pageable);
        } else {
            scripts = service.filterCategory(category, pageable);
        }

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        scripts,
                        "Scripts filtrados com sucesso"
                )
        );
    }

    // Buscar script por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCRIPT_VIEW')")
    public ResponseEntity<ApiResponse<ScriptResponseDTO>> findById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        service.findById(id),
                        "Script encontrado com sucesso"
                )
        );
    }

    // Atualizar script
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('SCRIPT_EDIT')")
    public ResponseEntity<ApiResponse<ScriptResponseDTO>> update(
            @PathVariable UUID id,
            @RequestBody @Valid ScriptUpdateDTO dto
    ) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        service.update(id, dto),
                        "Script atualizado com sucesso"
                )
        );
    }

    // Excluir script
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCRIPT_DELETE')")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id
    ) {
        service.delete(id);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        null,
                        "Script excluído com sucesso"
                )
        );
    }
}
