package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.Permission;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto.PermissionRequestDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto.PermissionResponseDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Mapper.PermissionMapper;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Repository.PermissionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PermissionsService {
    private PermissionsRepository repository;

    public PermissionsService(PermissionsRepository repository) {
        this.repository = repository;
    }

    public PermissionResponseDTO create(PermissionRequestDTO dto) {
        Permission permission = PermissionMapper.toEntity(dto);
        return PermissionMapper.toDTO(repository.save(permission));
    }

    private Permission findEntity(UUID id){

        return repository.findById(id)
                .orElseThrow(() -> new BusinessException("Permissão não encontrada"));
    }

    public List <PermissionResponseDTO> findAll(){
        List<Permission> permission = repository.findAll();

        return permission.stream().map(PermissionMapper::toDTO).toList();
    }

    public PermissionResponseDTO getById(UUID id) {
        Permission permission = findEntity(id);
        return PermissionMapper.toDTO(permission);
    }

    public void delete(UUID id) {
        Permission permission = findEntity(id);
        repository.delete(permission);
    }
}
