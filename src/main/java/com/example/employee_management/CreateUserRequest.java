package com.example.employee_management;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateUserRequest {
    @NotNull 
    @Positive 
    private Long companyId;
    @NotBlank 
    private String userName;
    @NotBlank 
    private String password;
    @NotBlank 
    @Email 
    private String email; 

    public CreateUserRequest(){

    }
    public Long getCompanyId(){
        return this.companyId;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getPassword(){
        return this.password;
    }
    public String getEmail(){
        return this.email;
    }
}