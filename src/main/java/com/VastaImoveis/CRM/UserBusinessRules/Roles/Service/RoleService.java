package com.VastaImoveis.CRM.UserBusinessRules.Roles.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto.RoleRequestDto;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto.RoleResponseDto;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Mapper.RoleMapper;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Repository.RoleRepository;

import java.util.UUID;

public class RoleService {
    private RoleRepository repository;

    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    private Role findEntity(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cargo não encontrado"));
    }

    private RoleResponseDto create(RoleRequestDto dto) {
        Role role = RoleMapper.toEntity(dto);
        return RoleMapper.toDTO(repository.save(role));
    }

    public RoleResponseDto findById(UUID id) {
        Role role = findEntity(id);

        return RoleMapper.toDTO(role);
    }

    public void delete(UUID id) {
        Role role = findEntity(id);

        repository.delete(role);
    }
}
