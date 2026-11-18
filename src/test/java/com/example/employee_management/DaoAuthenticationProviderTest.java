package com.example.employee_management;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class DaoAuthenticationProviderTest {
    @Mock
    UserDetailsService userDetailsService;
    PasswordEncoder passwordEncoder;
    DaoAuthenticationProvider daoAuthenticationProvider;

    @BeforeEach
    void setUp(){
        daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        passwordEncoder = new BCryptPasswordEncoder();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
    }
    @Test
    void shouldSuccessfullyAuthenticateUserPassword(){
        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername("testUser")
            .password(passwordEncoder.encode("myPassword123"))
            .authorities("ROLE_HR")
            .build();
        
        when (userDetailsService.loadUserByUsername("testUser"))
            .thenReturn(userDetails);
            
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "myPassword123");
        Authentication authentication = daoAuthenticationProvider.authenticate(authToken);

        assertTrue(authentication.isAuthenticated());
    }
    @Test
    void shouldRejectIncorrectPassword(){
        UserDetails userDetails = org.springframework.security.core.userdetails.User
            .withUsername("testUser")
            .password(passwordEncoder.encode("myPassword123"))
            .authorities("ROLE_HR")
            .build();
        
        when (userDetailsService.loadUserByUsername("testUser"))
            .thenReturn(userDetails);
            
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), "wrongPassword");

        assertThrows(
            BadCredentialsException.class,
            () -> daoAuthenticationProvider.authenticate(authToken)
        );
    }
}
