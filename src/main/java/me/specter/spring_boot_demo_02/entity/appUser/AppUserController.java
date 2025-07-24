package me.specter.spring_boot_demo_02.entity.appUser;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    @PostAuthorize("returnObject.username == authentication.name")
    public AppUserDto findUserById(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return this.appUserService.findByUsername(username);
    }
}
