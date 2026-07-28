package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Service;

import com.VastaImoveis.CRM.Exception.BusinessException;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.Permission;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto.PermissionRequestDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto.PermissionResponseDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Mapper.PermissionMapper;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Repository.PermissonsRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PermissionsService {
    private PermissonsRepository repository;

    public PermissionsService(PermissonsRepository repository) {
        this.repository = repository;
    }

    public PermissionResponseDTO create(PermissionRequestDTO dto) {
        Permission permission = PermissionMapper.toEntity(dto);
        return PermissionMapper.toDTO(repository.save(permission));
    }

    private Permission findEntity(UUID id){
        Permission permission = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Permissão não encontrada"));

        return  permission;
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
