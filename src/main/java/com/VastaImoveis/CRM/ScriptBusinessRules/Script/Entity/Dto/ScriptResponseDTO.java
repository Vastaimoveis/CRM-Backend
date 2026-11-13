package com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScriptResponseDTO {
    private UUID id;

    private String title;

    private String message;

    private UUID user;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ScriptResponseDTO(){}

    public ScriptResponseDTO(UUID id, String title, String message, UUID user, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
