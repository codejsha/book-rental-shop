package com.example.bookrentalshop.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserRegisterRequest {

    @Email
    @Size(min = 1, max = 50)
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    public void setEncryptedPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
}
