package com.example.employee_management;
import java.time.LocalDate;

public class CreateEmployeeRequest {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate hireDate;

    public CreateEmployeeRequest(){

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
}
