package com.example.employee_management;

public class CompanyNotFoundException extends RuntimeException {
    public CompanyNotFoundException(String message){
        super(message);
    }
}
