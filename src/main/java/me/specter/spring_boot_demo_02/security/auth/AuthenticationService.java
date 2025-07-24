package me.specter.spring_boot_demo_02.security.auth;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.entity.appRole.AppRoleRepository;
import me.specter.spring_boot_demo_02.entity.appRole.AppRole.RoleName;
import me.specter.spring_boot_demo_02.entity.appUser.AppUser;
import me.specter.spring_boot_demo_02.entity.appUser.AppUserRepository;
import me.specter.spring_boot_demo_02.entity.token.TokenService;
import me.specter.spring_boot_demo_02.exception.DataNotFoundException;
import me.specter.spring_boot_demo_02.exception.ErrorDescriptionConstants;
import me.specter.spring_boot_demo_02.exception.UserNotFoundException;
import me.specter.spring_boot_demo_02.security.JwtService;
import me.specter.spring_boot_demo_02.security.RegisterRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AppUserRepository appUserRepository;
    private final AppRoleRepository appRoleRepository;
    private final TokenService tokenService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    
    public void register(RegisterRequest request) {

        AppUser user = AppUser.builder()
        .id(null)
        .username(request.username())
        .email(request.email())
        .displayName(request.displayName())
        .password(this.passwordEncoder.encode(request.password()))
        .enabled(true)
        .createdAt(LocalDateTime.now())
        .lastModifiedAt(LocalDateTime.now())
        .roles(List.of(
            this.appRoleRepository
                .findByRoleName(RoleName.USER)
                .orElseThrow(() -> new DataNotFoundException("Role User is not found"))
        ))
        .build();

        this.appUserRepository.save(user);
    }

    public Optional<AuthenticationResponse> refreshAccessToken(String refreshToken) {
        if (refreshToken == null || !refreshToken.startsWith("Bearer ")) {
            return Optional.empty();
        }
        refreshToken = refreshToken.substring(7);

        final String username = this.jwtService.extractUsername(refreshToken);
        this.jwtService.isRefreshTokenValid(refreshToken, username);
        
        AppUser user = this.appUserRepository
            .findByUsername(username)
            .orElseThrow(() -> new UserNotFoundException(
                ErrorDescriptionConstants.USER_WITH_USERNAME_NOT_FOUND.formatted(username)
                )
            );
        String accessToken = this.jwtService.generateAccessToken(user);
        this.tokenService.revokeAllUserTokens(user);
        this.tokenService.saveUserToken(user, accessToken);
        
        return Optional.of(new AuthenticationResponse(accessToken, refreshToken));
    }

    public AuthenticationResponse login(AuthenticationRequest request) {
        log.info("authenticate via UsernamePasswordAuthenticationToken Begin");
         this.authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        log.info("authenticate via UsernamePasswordAuthenticationToken End");

        AppUser user = this.appUserRepository
            .findByUsername(request.username())
            .orElseThrow(
                () -> new UserNotFoundException(ErrorDescriptionConstants.USER_WITH_USERNAME_NOT_FOUND.formatted(request.username()))
            );     
        String jwtAccessToken = this.jwtService.generateAccessToken(user);
        String jwtRefreshToken = this.jwtService.generateRefreshToken(user);
        this.tokenService.revokeAllUserTokens(user);
        this.tokenService.saveUserToken(user, jwtAccessToken);
        return new AuthenticationResponse(jwtAccessToken, jwtRefreshToken);
    }
    
}
