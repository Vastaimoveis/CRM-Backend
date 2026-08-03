package com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto;

import com.VastaImoveis.CRM.UserBusinessRules.Roles.Entity.Domain.Role;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.RoleUsers;

import java.util.UUID;

public class UserResponseDTO {
    private UUID id;
    private String nome;
    private String email;
    private String telefone;
    private Role role;
    private RegiaoUsers regiao;

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public UserResponseDTO(UUID id, String nome, String email, String telefone, Role role, RegiaoUsers regiao) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.role = role;
        this.regiao = regiao;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public RegiaoUsers getRegiao() {
        return regiao;
    }

    public void setRegiao(RegiaoUsers regiao) {
        this.regiao = regiao;
    }
}
