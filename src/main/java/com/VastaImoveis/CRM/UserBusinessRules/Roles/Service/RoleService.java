package com.VastaImoveis.CRM.UserBusinessRules.Roles.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.Permission;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Repository.PermissionsRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto.RoleRequestDto;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto.RoleResponseDto;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Mapper.RoleMapper;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class RoleService {
    private RoleRepository repository;
    private PermissionsRepository permissionsRepository;
    public RoleService(RoleRepository repository) {
        this.repository = repository;
    }

    private Role findEntity(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Cargo não encontrado"));
    }

    public List<RoleResponseDto> findAll(){
        List<Role> roles = repository.findAll();
        return roles.stream().map(RoleMapper::toDTO).toList();
    }

    public RoleResponseDto create(RoleRequestDto request) {
        if (repository.existsByName(request.getName())) {
            throw new RuntimeException("Role já existe");
        }
        Set<Permission> permissions = request.getPermissions().stream()
                .map(name ->
                        permissionsRepository.findByName(PermissionName.valueOf(name))
                                .orElseThrow(() -> new RuntimeException("Permissão não encontrada: " + name)))
                .collect(java.util.stream.Collectors.toSet());
        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        role.setPermissions(permissions);
        repository.save(role);

        return RoleMapper.toDTO(role);
    }

    public RoleResponseDto findById(UUID id) {
        Role role = findEntity(id);

        return RoleMapper.toDTO(role);
    }

    public RoleResponseDto findByName(String name){
        Role role = repository.findByName(name)
                .orElseThrow(() -> new BusinessException("Cargo não encontrado"));
        return RoleMapper.toDTO(role);
    }

    public void delete(UUID id) {
        Role role = findEntity(id);

        repository.delete(role);
    }
}
