package com.example.employee_management;

import jakarta.validation.constraints.NotNull;

public class UpdateManagerRequest {
    @NotNull
    private Long managerId;

    public UpdateManagerRequest(){}

    public void setManagerId(Long managerId){
        this.managerId = managerId;
    }
    public Long getManagerId(){
        return this.managerId;
    }
}
