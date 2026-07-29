package com.VastaImoveis.CRM.Auth.Seeder;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.Permission;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Repository.PermissionsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Order(1)
public class PermissionSeeder {

    @Bean
    CommandLineRunner seedPermissions(
            PermissionsRepository repository
    ) {
        return args -> {
            for (PermissionName name : PermissionName.values()) {

                if (!repository.existsByName(name)) {

                    Permission permission = new Permission();

                    permission.setName(name);
                    permission.setDescription(name.name().replace("_", " "));

                    repository.save(permission);
                }
            }
        };
    }
}