package com.example.employee_management;

public interface UserService {
    
    public User createUser(Long companyId, String username, String password, String email);
    
}
