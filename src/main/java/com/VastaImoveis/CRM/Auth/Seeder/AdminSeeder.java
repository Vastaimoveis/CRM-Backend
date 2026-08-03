package com.VastaImoveis.CRM.Auth.Seeder;

import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Repository.RoleRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(3)
public class AdminSeeder {
    @Value("${ADMIN_EMAIL}")
    private String adminEmail;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Bean
    CommandLineRunner seedAdmin(UserRepository repo, RoleRepository roleRepository, PasswordEncoder encoder) {
        return args -> {
            if (repo.existsByEmail(adminEmail)) {
                return;
            }
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role ADMIN não encontrada"));
            User user = new User();
            user.setNome("Administrador");
            user.setEmail(adminEmail);
            user.setPassword(encoder.encode(adminPassword));
            user.setRegiao(RegiaoUsers.CURITIBA);
            user.setRole(adminRole);
            repo.save(user);
        };
    }
}
