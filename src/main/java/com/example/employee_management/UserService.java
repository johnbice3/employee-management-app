package com.example.employee_management;

public interface UserService {
    
    public User createUser(Long companyId, String userName, String password, String email);
    
}
