package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;

import java.util.UUID;

public class PermissionResponseDTO {
    private UUID id;
    private PermissionName name;
    private String description;

    public PermissionResponseDTO(UUID id, PermissionName name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public PermissionName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
