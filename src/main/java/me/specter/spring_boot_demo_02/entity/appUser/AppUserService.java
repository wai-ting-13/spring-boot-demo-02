package me.specter.spring_boot_demo_02.entity.appUser;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.exception.ErrorDescriptionConstants;
import me.specter.spring_boot_demo_02.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUserDto findByUsername(String username){
        return 
            this.appUserRepository
                .findByUsername(username)
                .map(u -> new AppUserDto(u))
                .orElseThrow(
                    () -> new UserNotFoundException(ErrorDescriptionConstants.USER_WITH_USERNAME_NOT_FOUND.formatted(username))
                );
    }
}
