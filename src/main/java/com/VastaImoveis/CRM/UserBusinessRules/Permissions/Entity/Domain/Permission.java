package com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(unique = true, nullable = false)
    private PermissionName name;

    private String description;

    public UUID getId() {
        return id;
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
