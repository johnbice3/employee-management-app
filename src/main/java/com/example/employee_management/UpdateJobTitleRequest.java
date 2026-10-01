package com.example.employee_management;
import jakarta.validation.constraints.NotBlank;

public class UpdateJobTitleRequest {
    @NotBlank 
    private String jobTitle;

    public UpdateJobTitleRequest(){}

    public void setJobTitle(String jobTitle){
        this.jobTitle = jobTitle;
    }
    public String getJobTitle(){
        return this.jobTitle;
    }
}
