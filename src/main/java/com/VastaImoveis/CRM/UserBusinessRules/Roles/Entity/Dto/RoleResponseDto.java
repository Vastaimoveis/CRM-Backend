package com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto;

import java.util.Set;
import java.util.UUID;

public class RoleResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Set<String> permissions;

    public RoleResponseDto(UUID id, String name, String description, Set<String> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<String> getPermissions() {
        return permissions;
    }
}
