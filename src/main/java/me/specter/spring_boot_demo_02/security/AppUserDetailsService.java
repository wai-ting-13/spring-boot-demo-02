package me.specter.spring_boot_demo_02.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import me.specter.spring_boot_demo_02.entity.appUser.AppUserRepository;
import me.specter.spring_boot_demo_02.exception.ErrorDescriptionConstants;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService{

    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.appUserRepository
            .findByUsername(username)
            .orElseThrow( () -> 
                new UsernameNotFoundException(ErrorDescriptionConstants.USER_WITH_USERNAME_NOT_FOUND.formatted(username)) 
            );
    }

}
