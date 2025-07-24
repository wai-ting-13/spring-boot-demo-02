package me.specter.spring_boot_demo_02.entity.appUser;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.specter.spring_boot_demo_02.entity.appRole.AppRole;
import me.specter.spring_boot_demo_02.entity.bookmark.Bookmark;
import me.specter.spring_boot_demo_02.entity.token.Token;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AppUser implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique=true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String displayName;
 
    @Column(unique=true, nullable = false)
    private String email;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean enabled;

    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime lastModifiedAt;

    // If not FetchType.EAGER, it fails when do getAuthorities() otherwise
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "app_user_role",
        joinColumns = @JoinColumn(name = "app_user_id"),
        inverseJoinColumns = @JoinColumn(name = "app_role_id")
    )
    private List<AppRole> roles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.LAZY)
    private List<Bookmark> bookmarks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Token> tokens;

    // Methods from UserDetails
    // We must add prefix "ROLE_"
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles
            .stream()
            .map( r -> new SimpleGrantedAuthority("ROLE_" + r.getRoleName()) )
            .toList()
        ;
    }
}
