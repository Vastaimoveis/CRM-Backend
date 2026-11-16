
package com.VastaImoveis.CRM.ScriptBusinessRules.Script.Service;

import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Domain.Script;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Domain.ScriptCategories;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptCreateDTO;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptResponseDTO;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto.ScriptUpdateDTO;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Mapper.ScriptMapper;
import com.VastaImoveis.CRM.ScriptBusinessRules.Script.Repository.ScriptRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;

import com.VastaImoveis.CRM.shared.utils.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class ScriptService {

    private final ScriptRepository repository;

    public ScriptService(ScriptRepository repository) {
        this.repository = repository;
    }

    private User getCurrentUser() {
        User user = SecurityUtils.getCurrentUser();

        if (user == null) {
            throw new IllegalStateException("Usuário não autenticado.");
        }

        return user;
    }

    private Script getOwnedScript(UUID id, UUID userId) {
        return repository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Script não encontrado.")
                );
    }

    @Transactional
    public ScriptResponseDTO create(ScriptCreateDTO dto) {

        User user = getCurrentUser();

        Script script = ScriptMapper.toEntity(dto);
        script.setUser(user);

        return ScriptMapper.toResponse(repository.save(script));
    }

    @Transactional(readOnly = true)
    public Page<ScriptResponseDTO> findAll(Pageable pageable) {

        User user = getCurrentUser();

        return repository.findAllByUser_Id(user.getId(), pageable)
                .map(ScriptMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<ScriptResponseDTO> filterCategory(ScriptCategories category, Pageable pageable) {
        User user = getCurrentUser();

        return repository.findByUser_IdAndCategory(user.getId(), category, pageable)
                .map(ScriptMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public ScriptResponseDTO findById(UUID id) {

        User user = getCurrentUser();

        Script script = getOwnedScript(id, user.getId());

        return ScriptMapper.toResponse(script);
    }

    @Transactional(readOnly = true)
    public Page<ScriptResponseDTO> search(
            String search,
            Pageable pageable
    ) {

        User user = getCurrentUser();

        if (search == null || search.isBlank()) {
            return findAll(pageable);
        }

        return repository.searchByUser(
                user.getId(),
                search.trim(),
                pageable
        ).map(ScriptMapper::toResponse);
    }

    @Transactional
    public ScriptResponseDTO update(UUID id, ScriptUpdateDTO dto) {

        User user = getCurrentUser();

        Script script = getOwnedScript(id, user.getId());

        ScriptMapper.toUpdate(script, dto);

        return ScriptMapper.toResponse(repository.save(script));
    }

    @Transactional
    public void delete(UUID id) {

        User user = getCurrentUser();

        Script script = getOwnedScript(id, user.getId());

        repository.delete(script);
    }
}
