package me.specter.spring_boot_demo_02.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter{

    @Value("${application.security.auth-api-prefix}")
    private String AUTH_API_URL;

    private final AppUserDetailsService appUserDetailsService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response, 
        @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        
        log.info("JWT Filtering Begin");
        

        if (request.getServletPath().contains(AUTH_API_URL)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtToken;
        final String username;

        log.info("authHeader is " + authHeader);
        
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            log.info("The authorisation header does not contain JWT");
            filterChain.doFilter(request, response);
            return;
        }
       
        jwtToken = authHeader.substring(7);
        
        // find UserDeails
        username = this.jwtService.extractUsername(jwtToken);
        final UserDetails userDetails = this.appUserDetailsService.loadUserByUsername(username);

        // Validate JWT and Username
        if (this.jwtService.isAccessTokenValid(jwtToken, userDetails.getUsername())) {
            final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
            );
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        log.info("JWT Filtering End");
        filterChain.doFilter(request, response);   
    }
}
