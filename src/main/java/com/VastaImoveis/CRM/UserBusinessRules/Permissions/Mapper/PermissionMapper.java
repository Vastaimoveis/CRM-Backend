package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Mapper;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.Permission;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto.PermissionRequestDTO;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto.PermissionResponseDTO;

public class PermissionMapper {
    public static PermissionResponseDTO toDTO(Permission permission) {
        return new PermissionResponseDTO(
                permission.getId(),
                permission.getName(),
                permission.getDescription()
        );
    }

    public static Permission toEntity(PermissionRequestDTO dto){
        Permission permission = new Permission();
        permission.setName(dto.getName());
        permission.setDescription(dto.getDescription());
        return permission;
    }
}
