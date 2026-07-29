package com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.dto;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.RegiaoUsers;
import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.RoleUsers;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class UserRequestDTO {
    @NotBlank
    private String nome;

    @Email(message = "Adicione um email válido")
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String telefone;

    public String getTelefone() {
        return telefone;
    }

    private UUID role;

    private RegiaoUsers regiao;

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UUID getRole() {
        return role;
    }

    public RegiaoUsers getRegiao() {
        return regiao;
    }
}
