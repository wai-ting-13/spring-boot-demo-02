package me.specter.spring_boot_demo_02.entity.appUser;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("api/v1/internal/users")
@RequiredArgsConstructor
public class AppUserControllerInternal {
    private final AppUserServiceInternal appUserServiceInternal;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    public List<AppUserDto> findAll() {
        return this.appUserServiceInternal.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public AppUserDto findById(@PathVariable Integer id){
        return this.appUserServiceInternal.findById(id);
    }

    // Url: http://localhost:8080/api/v1/internal/users/{id}?enabled=<#true or false#>
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public void changeUserEnabledStatus(
        @PathVariable Integer id, 
        @RequestParam(
            required = true, 
            defaultValue = "true"
        ) 
        boolean enabled
    ) {
        this.appUserServiceInternal.changeUserEnabledStatus(id, enabled);
    }
}

