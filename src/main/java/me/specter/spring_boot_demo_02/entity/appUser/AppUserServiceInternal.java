package me.specter.spring_boot_demo_02.entity.appUser;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.specter.spring_boot_demo_02.entity.appRole.AppRole.RoleName;
import me.specter.spring_boot_demo_02.exception.ErrorDescriptionConstants;
import me.specter.spring_boot_demo_02.exception.UserNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserServiceInternal {
    private final AppUserRepository appUserRepository;
   
    public List<AppUserDto> findAll(){
        return this.appUserRepository.findAll()
            .stream()
            .map(u -> new AppUserDto(u))
            .collect(Collectors.toList());
    }

    public AppUserDto findById(Integer id){
        return 
            this.appUserRepository
                .findById(id)
                .map(u -> new AppUserDto(u))
                .orElseThrow(
                    () -> new UserNotFoundException(ErrorDescriptionConstants.USER_WITH_ID_NOT_FOUND.formatted(id))
                );
    }

    public void changeUserEnabledStatus(Integer id, boolean enable){
        AppUser user =  this.appUserRepository
            .findById(id)
            .filter( 
                // MANAGER cannot disable another MANAGER or ADMIN
                u -> u.getRoles().stream().noneMatch( 
                    r -> r.getRoleName() == RoleName.ADMIN 
                    || r.getRoleName() == RoleName.MANAGER
                    )
            ).orElseThrow(
                () ->new UserNotFoundException(ErrorDescriptionConstants.USER_WITH_ID_NOT_FOUND.formatted(id))
            );
        user.setEnabled(enable);
        this.appUserRepository.save(user);
    }
}
