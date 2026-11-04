package com.example.employee_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest 
public class UserRepositoryTest {
    @Autowired 
    private UserRepository userRepository;
    @Autowired 
    private CompanyRepository companyRepository;

    @Test
    void shouldCreateNewCompanyAndUser(){
        Company testCompany = companyRepository.save(new Company("Development Company"));
        userRepository.save(new User(testCompany, "testUser1", "testPassword","testUser1@test.com"));

        User result = userRepository.findByUserName("testUser1").orElseThrow();
        User resultEmail = userRepository.findByEmail("testUser1@test.com").orElseThrow();

        assertEquals("testUser1", result.getUserName());
        assertEquals("testUser1@test.com", resultEmail.getEmail());
        
    }
    @Test
    void shouldRejectDuplicateEmail(){
        Company testCompany = companyRepository.save(new Company("Development Company"));
        userRepository.save(new User(testCompany, "testUser1", "testPassword","testUser1@test.com"));
        userRepository.flush();
        
        assertThrows(
            DataIntegrityViolationException.class,
            () -> userRepository.save(new User(testCompany, "testUser2", "testPassword","testUser1@test.com"))
        );
    }
    @Test
    void shouldRejectDuplicateUserName(){
        Company testCompany = companyRepository.save(new Company("Development Company"));
        userRepository.save(new User(testCompany, "testUser1", "testPassword","testUser1@test.com"));
        userRepository.flush();
                
        assertThrows(
            DataIntegrityViolationException.class,
            () -> userRepository.save(new User(testCompany, "testUser1", "testPassword","testUser2@test.com"))
        );
    }
}
