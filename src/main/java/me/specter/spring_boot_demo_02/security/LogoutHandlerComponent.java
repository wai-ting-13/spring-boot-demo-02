package me.specter.spring_boot_demo_02.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.specter.spring_boot_demo_02.entity.token.TokenRepository;

@Component
@RequiredArgsConstructor
public class LogoutHandlerComponent implements LogoutHandler{
    private final TokenRepository tokenRepository;

    // Revoke current access tokens
    @Override
    public void logout(
        HttpServletRequest request, 
        HttpServletResponse response, 
        Authentication authentication
    ) {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }
        jwtToken = authHeader.substring(7);
        
        this.tokenRepository.findByToken(jwtToken)
            .stream()
            .forEach( 
                t -> {
                    t.setRevoked(true);
                    this.tokenRepository.save(t);
                } 
            );
    }

    
}
