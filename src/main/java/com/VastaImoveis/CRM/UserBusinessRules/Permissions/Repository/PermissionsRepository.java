package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Repository;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.Permission;
import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionsRepository extends JpaRepository<Permission, UUID> {
    Optional<Permission> findByName(PermissionName name);
    boolean existsByName(PermissionName name);
}
