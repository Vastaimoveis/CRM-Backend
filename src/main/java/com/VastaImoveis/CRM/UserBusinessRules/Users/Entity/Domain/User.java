package com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain;

import com.VastaImoveis.CRM.UserBusinessRules.Permissions.Entity.Domain.PermissionName;
import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column
    private String telefone;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "newRole")
    private Role newRole;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "role_id", nullable = false)
    private RoleUsers role;

    @Enumerated(EnumType.STRING)
    @Column(name = "regiao", nullable = false)
    private RegiaoUsers regiao;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.regiao == null) {
            this.regiao = RegiaoUsers.CURITIBA;
        }
    }

    public UUID getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return newRole;
    }

    public void setRole(Role role) {
        this.newRole = role;
    }

    public RegiaoUsers getRegiao() {
        return regiao;
    }

    public void setRegiao(RegiaoUsers regiao) {
        this.regiao = regiao;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean hasPermission(PermissionName permission) {
        return newRole.getPermissions().stream().anyMatch(p -> p.getName() == permission);
    }

    public RoleUsers getLegacyRole() {
        return role;
    }

    public void setLegacyRole(RoleUsers legacyRole) {
        this.role = legacyRole;
    }
}
