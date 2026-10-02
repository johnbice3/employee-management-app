package com.example.employee_management;
import jakarta.validation.constraints.NotBlank;

public class UpdateDepartmentRequest {
    @NotBlank 
    private String departmentCode;
    @NotBlank 
    private String departmentName;

    public UpdateDepartmentRequest(){}

    public void setDepartmentCode(String departmentCode){
        this.departmentCode = departmentCode;
    }
    public void setDepartmentName(String departmentName){
        this.departmentName = departmentName;
    }

    public String getDepartmentCode(){
        return this.departmentCode;
    }

    public String getDepartmentName(){
        return this.departmentName;
    }
}
