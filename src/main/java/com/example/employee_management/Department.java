package com.example.employee_management;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable 
public class Department {
    @Column(name = "department_code", length = 50)
    private String departmentCode;
    @Column(name = "department_name", length = 50)
    private String departmentName;

    public Department(String departmentCode, String departmentName){
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
    }
    protected Department(){}

    public String getDepartmentCode(){
        return this.departmentCode;
    }
    public String getDepartmentName(){
        return this.departmentName;
    }
}
