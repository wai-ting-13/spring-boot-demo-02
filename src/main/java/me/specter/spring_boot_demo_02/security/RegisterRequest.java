package me.specter.spring_boot_demo_02.security;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
    @NotBlank String username,
    @NotBlank String password,
    @NotBlank String displayName,
    @NotBlank @Email String email
) { }
