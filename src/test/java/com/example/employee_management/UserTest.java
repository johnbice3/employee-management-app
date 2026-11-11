package com.example.employee_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class UserTest {
    
    @Test 
    void shouldCreateABasicUser(){
        Company testCompany = new Company("Test Company");
        
        User basicUser = new User(testCompany, "testUser", "rawPassword", "testUser@test.com");

        assertEquals(CompanyRole.BASIC, basicUser.getCompanyRole());
    }

    @Test
    void shouldCreateAnHrUser(){
        Company testCompany = new Company("Test Company");
        User hrUser = new User(testCompany, "hrTestUser", "rawPassword", "hrTestUser@test.com", CompanyRole.HR);

        assertEquals(CompanyRole.HR, hrUser.getCompanyRole());
    }
    
}
