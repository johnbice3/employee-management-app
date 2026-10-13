package com.example.employee_management;
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateEmployeeRequest {
    @NotNull
    private Company employer;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Email 
    private String email;
    @NotNull
    private LocalDate hireDate;
    private String employeeNumber;

    public CreateEmployeeRequest(){

    }
    public void setEmployer(Company employer){
        this.employer = employer;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public void setHireDate(LocalDate hireDate){
        this.hireDate = hireDate;
    }
    public void setEmployeeNumber(String employeeNumber){
        this.employeeNumber = employeeNumber;
    }
    public Company getEmployer(){
        return this.employer;
    }
    public String getFirstName(){
        return this.firstName;
    }
    public String getLastName(){
        return this.lastName;
    }
    public String getEmail(){
        return this.email;
    }
    public LocalDate getHireDate(){
        return this.hireDate;
    }
    public String getEmployeeNumber(){
        return this.employeeNumber;
    }
}
