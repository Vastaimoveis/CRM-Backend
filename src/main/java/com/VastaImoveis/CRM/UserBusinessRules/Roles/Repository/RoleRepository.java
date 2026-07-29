package com.VastaImoveis.CRM.UserBusinessRules.Roles.Repository;

import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
