package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Repository;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PermissonsRepository extends JpaRepository<Permission, UUID> {
    
}
