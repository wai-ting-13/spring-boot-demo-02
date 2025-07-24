package me.specter.spring_boot_demo_02.security.auth;

public record AuthenticationResponse (
    String accessToken,
    String refreshToken
){ }
