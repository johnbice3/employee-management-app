package com.example.employee_management;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String jobTitle;
    private int departmentNumber;
    private String departmentName;
    private BigDecimal salary;
    private LocalDate hireDate;

    public Employee(String firstName, String lastName, String email, LocalDate hireDate){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.hireDate = hireDate;
    }

    protected Employee(){

    }

    public Long getId(){
        return this.id;
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
    public String getJobTitle(){
        return this.jobTitle;
    }
    public int getDepartmentNumber(){
        return this.departmentNumber;
    }
    public String getDepartmentName(){
        return this.departmentName;
    }
    public LocalDate getHireDate(){
        return this.hireDate;
    }
    public BigDecimal getSalary(){
        return this.salary;
    }
    public void setSalary(BigDecimal salary){
        this.salary = salary;
    }
    public void setDepartment(int departmentNumber, String departmentName){
        this.departmentNumber = departmentNumber;
        this.departmentName = departmentName;
    }
    public void setJobTitle(String jobTitle){
        this.jobTitle = jobTitle;
    }
}
