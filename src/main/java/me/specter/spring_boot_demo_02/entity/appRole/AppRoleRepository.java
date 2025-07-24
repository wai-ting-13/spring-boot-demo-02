package me.specter.spring_boot_demo_02.entity.appRole;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.specter.spring_boot_demo_02.entity.appRole.AppRole.RoleName;

@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Integer> {
    Optional<AppRole> findByRoleName(RoleName roleName);
}
