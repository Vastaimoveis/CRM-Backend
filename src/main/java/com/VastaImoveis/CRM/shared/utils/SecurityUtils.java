package com.VastaImoveis.CRM.shared.utils;


import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof User user)) {
            return null;
        }

        return user;
    }

    public static boolean isGerente(){
        return  getCurrentUser().getRole().name().equals("GERENTE");
    }
}