package me.specter.spring_boot_demo_02.entity.token;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.entity.appUser.AppUser;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final TokenRepository tokenRepository;

    public boolean isTokenInDatabaseValid(String jwtToken){
        return this.tokenRepository
            .findByToken(jwtToken)
            .map(t -> !t.isRevoked())
            .orElse(false)
        ;
    }

    public void revokeAllUserTokens(AppUser user){
        List<Token> validTokens = this.tokenRepository.findAllValidToken(user.getId());
        if (!validTokens.isEmpty()){
            validTokens.stream()
                .forEach( 
                    token -> {
                        token.setRevoked(true);
                    }
                );
            this.tokenRepository.saveAll(validTokens);
        }
    }

    public void saveUserToken(AppUser user, String jwtToken) {
        log.info("Saved jwtToken=%s".formatted(jwtToken));
        Token tokenInDatabase = 
            Token
                .builder()
                .id(null)
                .token(jwtToken)
                .user(user)
                .revoked(false)
                .build();

        this.tokenRepository.save(tokenInDatabase);
    }
}
