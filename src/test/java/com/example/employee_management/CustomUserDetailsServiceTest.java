package com.example.employee_management;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CustomUserDetailsServiceTest {
    @Mock 
    UserRepository userRepository;
    CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    void setUp(){
        customUserDetailsService = new CustomUserDetailsService(userRepository);
    }

    @Test 
    void validUsernameShouldReturnCorrectUser(){
        Company testCompany = new Company("Test Company");
        User testUser = new User(testCompany, "testUser", "rawPassword", "test@test.com", CompanyRole.HR);

        when (userRepository.findByUserName("testUser"))
            .thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUser");

        assertEquals("testUser", userDetails.getUsername());
        assertEquals("rawPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities()
            .stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_HR")));
    }
    @Test 
    void unknownUsernameShouldReturnUsernameNotFoundException(){
        when (userRepository.findByUserName("testUser"))
            .thenReturn(Optional.empty());
        
        UsernameNotFoundException exception = assertThrows(
            UsernameNotFoundException.class,
            () -> customUserDetailsService.loadUserByUsername("testUser")
        );
        assertEquals("That username does not exist.", exception.getMessage());
    }
    @Test
    void disabledUserShouldReturnDisabledUserDetails(){
        Company testCompany = new Company("Test Company");
        User testUser = new User(testCompany, "testUser", "rawPassword", "test@test.com", CompanyRole.HR);
        testUser.setDisabled();

        when (userRepository.findByUserName("testUser"))
            .thenReturn(Optional.of(testUser));

        UserDetails userDetails = customUserDetailsService.loadUserByUsername("testUser");
        assertFalse(userDetails.isEnabled());
    }
}
