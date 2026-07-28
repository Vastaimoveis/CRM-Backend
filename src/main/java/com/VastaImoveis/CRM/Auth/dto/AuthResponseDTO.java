package com.VastaImoveis.CRM.Auth.dto;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto.UserResponseDTO;

public class AuthResponseDTO {
    private String accessToken;
    private String refreshToken;
    private UserResponseDTO user;

    public AuthResponseDTO(UserResponseDTO user, String token, String refreshToken) {
        this.user = user;
        this.accessToken = token;
        this.refreshToken = refreshToken;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

}
