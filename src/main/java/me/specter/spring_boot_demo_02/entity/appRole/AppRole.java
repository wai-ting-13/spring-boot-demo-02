package me.specter.spring_boot_demo_02.entity.appRole;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AppRole {
    @Id
    private Integer id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public enum RoleName{
        USER,
        MANAGER,
        ADMIN,
    }
}
