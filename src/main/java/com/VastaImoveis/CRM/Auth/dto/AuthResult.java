package com.VastaImoveis.CRM.Auth.dto;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;

public record AuthResult(String accessToken,
                         String refreshToken,
                         User user) {
}
