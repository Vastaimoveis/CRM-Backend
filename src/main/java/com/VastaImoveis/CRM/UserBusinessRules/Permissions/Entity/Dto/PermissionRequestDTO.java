package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Dto;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;
import jakarta.validation.constraints.NotNull;

public class PermissionRequestDTO {
    @NotNull
    private PermissionName name;

    private String description;

    public PermissionRequestDTO(PermissionName nome, String description) {
        this.name = nome;
        this.description = description;
    }

    public PermissionName getName() {
        return name;
    }

    public void setName(PermissionName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
