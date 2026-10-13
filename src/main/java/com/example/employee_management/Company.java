package com.example.employee_management;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;




@Entity
public class Company {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;

    public Company(String companyName){
        this.companyName = companyName;
    }
    protected Company(){

    }

    public Long getId(){
        return this.id;
    }
    public String getCompanyName(){
        return this.companyName;
    }
}
