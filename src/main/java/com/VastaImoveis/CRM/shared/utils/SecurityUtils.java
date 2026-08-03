package com.VastaImoveis.CRM.shared.utils;


import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    private static UserRepository userRepository;

    public SecurityUtils(UserRepository userRepository) {
        SecurityUtils.userRepository = userRepository;
    }

    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        String email = authentication.getName();
        if (email == null || email.equals("anonymousUser")) {
            return null;
        }
        return userRepository.findByEmail(email).orElse(null);
    }

    public static boolean isGerente() {
        User user = getCurrentUser();
        return user != null && ("GERENTE".equals(user.getRole().getName()) || "ADMIN".equals(user.getRole().getName()));
    }
}