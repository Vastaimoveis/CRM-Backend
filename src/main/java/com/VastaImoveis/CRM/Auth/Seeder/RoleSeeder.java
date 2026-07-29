package com.VastaImoveis.CRM.Auth.Seeder;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.Permission;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Repository.PermissionsRepository;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@Order(2)
public class RoleSeeder {
    @Bean
    CommandLineRunner seedRoles(RoleRepository roleRepository, PermissionsRepository permissionRepository) {
        return args -> {
            createRoleIfNotExists(
                    roleRepository,
                    permissionRepository,
                    "ADMIN",
                    "Administrador do sistema",
                    Set.of(PermissionName.values()));
            createRoleIfNotExists(
                    roleRepository,
                    permissionRepository,
                    "GERENTE",
                    "Gerente comercial",
                    Set.of(
                            PermissionName.LEAD_VIEW,
                            PermissionName.LEAD_CREATE,
                            PermissionName.LEAD_EDIT,
                            PermissionName.LEAD_EXPORT,
                            PermissionName.REPORT_VIEW, PermissionName.USER_VIEW
                    ));
            createRoleIfNotExists(
                    roleRepository,
                    permissionRepository,
                    "CORRETOR",
                    "Corretor de imóveis",
                    Set.of(
                            PermissionName.LEAD_VIEW,
                            PermissionName.LEAD_CREATE,
                            PermissionName.LEAD_EDIT,
                            PermissionName.REMINDER_CREATE,
                            PermissionName.REMINDER_EDIT
                    ));
        };
    }

    private void createRoleIfNotExists(
            RoleRepository roleRepository,
            PermissionsRepository permissionRepository,
            String name,
            String description,
            Set<PermissionName> permissionNames) {
        if (roleRepository.existsByName(name)) {
            return;
        }
        Set<Permission> permissions = permissionNames
                .stream()
                .map(permissionName ->
                        permissionRepository.findByName(permissionName)
                                .orElseThrow(() ->
                                        new RuntimeException("Permissão não encontrada: " + permissionName))).collect(Collectors.toSet());
        Role role = new Role();
        role.setName(name);
        role.setDescription(description);
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
}