package com.example.employee_management;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    Company testCompany = new Company("Test Company");
    
    @Mock 
    PasswordEncoder encoder;
    @Mock 
    UserRepository userRepository;
    @Mock 
    CompanyRepository companyRepository;
    @InjectMocks 
    UserServiceImpl userServiceImpl;

    @Test 
    void shouldSuccessfullyCreateNewUser() {

        when (companyRepository.findById(1L))
            .thenReturn(Optional.of(testCompany)); 
        when (userRepository.findByUserName("jbice1"))
            .thenReturn(Optional.empty());
        when (userRepository.findByEmail("test@test.com"))
            .thenReturn(Optional.empty());
        when (encoder.encode("rawPassword"))
            .thenReturn("encodedPassword");
        when (userRepository.save(any(User.class)))
            .thenAnswer(invocation -> {
                User createdUser = invocation.getArgument(0);
                return createdUser;
            });
        User user = userServiceImpl.createUser(1L, "jbice1", "rawPassword", "test@test.com");
        
        assertEquals("jbice1", user.getUserName());
        assertEquals("test@test.com", user.getEmail());
        assertEquals(testCompany, user.getEmployer());
        assertNotEquals("rawPassword", user.getPassword());
        assertEquals("encodedPassword", user.getPassword());
        verify(userRepository).save(user);
    }
    @Test 
    void shouldThrowExceptionWhenCompanyDoesNotExist(){

        when (companyRepository.findById(1L))
            .thenReturn(Optional.empty());
        CompanyNotFoundException exception = assertThrows(
            CompanyNotFoundException.class,
            () -> userServiceImpl.createUser(1L, "jbice1", "rawPassword", "test@test.com")
        );

        assertEquals("Company does not exist with the provided ID", exception.getMessage());  
        verify(userRepository, never()).save(any(User.class));  
    }
    @Test 
    void shouldThrowExceptionWhenUsernameAlreadyExists(){
        User existingUser = new User(testCompany, "jbice1", "rawPassword", "test@test.com");
        
        when (companyRepository.findById(1L))
            .thenReturn(Optional.of(testCompany));
        when (userRepository.findByUserName(existingUser.getUserName()))
            .thenReturn(Optional.of(existingUser));
        
        UserAlreadyExistsException exception = assertThrows(
            UserAlreadyExistsException.class,
            () -> userServiceImpl.createUser(1L, "jbice1", "rawPassword", "test@test.com")
        );

        assertEquals("That username is already associated with a user.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
    @Test 
    void shouldThrowExceptionWhenEmailAlreadyExists(){
        User existingUser = new User(testCompany, "jbice1", "rawPassword", "test@test.com");
        
        when (companyRepository.findById(1L))
            .thenReturn(Optional.of(testCompany));
        when (userRepository.findByUserName(existingUser.getUserName()))
            .thenReturn(Optional.empty());
        when (userRepository.findByEmail("test@test.com"))
            .thenReturn(Optional.of(existingUser));
        
        UserAlreadyExistsException exception = assertThrows(
            UserAlreadyExistsException.class,
            () -> userServiceImpl.createUser(1L, "jbice1", "rawPassword", "test@test.com")
        );

        assertEquals("That email is already associated with a user.", exception.getMessage());
        verify(userRepository, times(1)).findByEmail("test@test.com");
        verify(userRepository, never()).save(any(User.class));
    }
}
