package me.specter.spring_boot_demo_02.security.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationRequest(
    @NotBlank String username,
    @NotBlank String password
) {}
