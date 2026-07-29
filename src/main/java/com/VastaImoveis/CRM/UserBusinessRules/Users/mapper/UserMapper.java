package com.VastaImoveis.CRM.UserBusinessRules.Users.mapper;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.UserResponseDTO;

public class UserMapper {
    public static UserResponseDTO toDTO(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTelefone(),
                user.getRole().getId(),
                user.getRegiao()
        );
    }

}
