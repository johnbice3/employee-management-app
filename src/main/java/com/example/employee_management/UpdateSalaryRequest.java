package com.example.employee_management;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class UpdateSalaryRequest {
    @Positive
    @NotNull
    private BigDecimal salary;

    public UpdateSalaryRequest(){

    }
    public void setSalary(BigDecimal salary){
        this.salary = salary;
    }
    public BigDecimal getSalary(){
        return this.salary;
    }
}
