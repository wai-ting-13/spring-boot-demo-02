package me.specter.spring_boot_demo_02.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.entity.token.TokenService;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {
    
    private final TokenService tokenService;
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Value("${application.security.jwt.access-token.expiration}")
    private long ACCESS_TOKEN_EXPIRATION;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long REFRESH_TOKEN_EXPIRATION;

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        try {
            return Jwts
                .parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        } catch (final JwtException e){
            throw new RuntimeException("Invalid token", e);
        }
    }

    public String extractUsername(String jwtToken) {
        return extractClaims(jwtToken, Claims::getSubject);
    }

    public boolean isAccessTokenValid(String jwtToken, String username) {
        log.info("validate access token");
        return this.validateToken(jwtToken, username, false);
    }

    public boolean isRefreshTokenValid(String jwtToken, String username) {
        log.info("validate refresh token");
        return this.validateToken(jwtToken, username, true);
    }

    public boolean validateToken(String jwtToken, String usernameInUserDetails, boolean isTokenNotInDB){

        final String usernameInToken = this.extractUsername(jwtToken);
        final Date expiration = extractClaims(jwtToken, Claims::getExpiration);
        final Date issuedAt = extractClaims(jwtToken, Claims::getIssuedAt);
        final Date now = new Date();

        boolean isTokenInDatabaseValid = 
            isTokenNotInDB 
            || this.tokenService.isTokenInDatabaseValid(jwtToken)
        ;

        log.info("isTokenInDatabaseValid = %b".formatted(isTokenInDatabaseValid));
        
        boolean isTokenReceivedValid = 
            usernameInToken.equals(usernameInUserDetails)
            && now.before(expiration) 
            && now.after(issuedAt)
        ;

        log.info("isTokenReceivedValid = %b".formatted(isTokenReceivedValid));

        return isTokenInDatabaseValid && isTokenReceivedValid;
    }

    public String generateAccessToken(UserDetails userDetails){

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", userDetails.getAuthorities().stream().map(role -> role.getAuthority()).toList());
        return generateAccessToken(extraClaims , userDetails);
    }

    public String generateAccessToken(
        Map<String, Object> extraClaims,
        UserDetails userDetails
    ){
        return buildJwtToken(extraClaims, userDetails, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(
        UserDetails userDetails
    ){
        return buildJwtToken(Collections.emptyMap(), userDetails, REFRESH_TOKEN_EXPIRATION);
    }

    private String buildJwtToken(
        Map<String, Object> extraClaims, 
        UserDetails userDetails,
        long expiration
    ) {
        return Jwts.builder()
        .claims(extraClaims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + expiration) )
        .signWith(this.privateKey)
        .compact();
    }
}
