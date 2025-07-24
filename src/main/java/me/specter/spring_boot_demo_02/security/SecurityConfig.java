package me.specter.spring_boot_demo_02.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    @Value("${application.security.allowed-api-patterns}")
    private String [] ALLOWING_LIST;

    @Value("${application.security.blocked-api-patterns}")
    private String [] BLOCKING_LIST;

    private final JwtFilter jwtFilter;

    private final LogoutHandlerComponent logoutHandlerComponent;

    @Bean SecurityFilterChain filterChain(final HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
            // Stateless => No CSRF Protection Need
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> 
                auth
                    .requestMatchers(ALLOWING_LIST).permitAll()
                    .requestMatchers(BLOCKING_LIST).denyAll()
                    .anyRequest().authenticated()
            ).sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            ).authenticationProvider(authenticationProvider)
            .addFilterBefore(this.jwtFilter, UsernamePasswordAuthenticationFilter.class)
            .logout(logout ->
                        logout
                                .logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(this.logoutHandlerComponent)
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder.clearContext()
                                )
                )
        ;
        
        return http.build();
    }

    @Bean AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return authenticationProvider;
    }


    @Bean AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // Add PasssowrdEncoder Bean here
    @Bean PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
}
