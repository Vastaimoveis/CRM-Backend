package com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Dto;

import java.util.UUID;

public class RoleResponseDto {
    private UUID id;
    private String name;
    private String description;

    public RoleResponseDto(UUID id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
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
}
