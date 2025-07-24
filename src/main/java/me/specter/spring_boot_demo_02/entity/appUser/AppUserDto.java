package me.specter.spring_boot_demo_02.entity.appUser;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class AppUserDto {
    private Integer id;
    private String username;
    private String displayName;
    private String email;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public AppUserDto(AppUser appUser) {
        this.id = appUser.getId();
        this.username = appUser.getUsername();
        this.displayName = appUser.getDisplayName();
        this.email = appUser.getEmail();
        this.enabled = appUser.isEnabled();
        this.createdAt = appUser.getCreatedAt();
        this.lastModifiedAt = appUser.getLastModifiedAt();
    }
}
