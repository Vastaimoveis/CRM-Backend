package com.VastaImoveis.CRM.UserBusinessRules.Roles.Mapper;

import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto.RoleRequestDto;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto.RoleResponseDto;

public class RoleMapper {
    public static Role toEntity(RoleRequestDto dto){
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        return role;
    }

    public static RoleResponseDto toDTO(Role role){
        return new RoleResponseDto(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}
