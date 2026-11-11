package com.example.employee_management;

public class UserResponse {
    private Long id;
    private String userName;
    private String email;

    public UserResponse(Long id, String userName, String email){
        this.id = id;
        this.userName = userName;
        this.email = email;
    }
    public Long getId(){
        return this.id;
    }
    public String getUserName(){
        return this.userName;
    }
    public String getEmail(){
        return this.email;
    }
}
