package me.specter.spring_boot_demo_02.appUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import me.specter.spring_boot_demo_02.entity.appUser.AppUser;
import me.specter.spring_boot_demo_02.entity.appUser.AppUserDto;
import me.specter.spring_boot_demo_02.entity.appUser.AppUserRepository;
import me.specter.spring_boot_demo_02.entity.appUser.AppUserServiceInternal;

public class AppUserServiceTest {
    
    @InjectMocks
    private AppUserServiceInternal appUserServiceInternal;

    @Mock
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void returnAllUser(){
        // Given
        AppUser appUser = 
            AppUser
                .builder()
                .id(1)
                .username("peter.chan")
                .password("")
                .displayName("Peter Chan")
                .email("peter.chan@example.com")
                .enabled(true)
                .build();
        AppUserDto expected = new AppUserDto(appUser);

        // Mock the calls
        when(appUserRepository.findAll()).thenReturn(List.of(appUser));

        // When
        AppUserDto actual = appUserServiceInternal.findAll().get(0);

        // Then
        // Ensure this method executes only once
        verify(appUserRepository, times(1)).findAll();

        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDisplayName(), actual.getDisplayName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

}
