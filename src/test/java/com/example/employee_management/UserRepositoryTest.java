package com.example.employee_management;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
