package com.VastaImoveis.CRM.ScriptBusinessRules.Script.Entity.Dto;

import com.VastaImoveis.CRM.UserBusinessRules.Users.Entity.Domain.User;
import jakarta.validation.constraints.Size;

public class ScriptUpdateDTO {

    @Size(max = 150)
    private String title;

    @Size(max = 5000)
    private String message;
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
