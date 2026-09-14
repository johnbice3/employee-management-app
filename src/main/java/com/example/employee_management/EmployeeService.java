package com.example.employee_management;

import java.util.List;
import java.math.BigDecimal;

public interface EmployeeService {
    
    public Employee createEmployee(String firstName, String lastName, String email);

    public Employee getEmployeeInfo(Long id);

    public List<Employee> listAllEmployees();

    public void updateSalary(Long id, BigDecimal salary);

    public void updateJobTitle(Long id, String jobTitle);

    public void updateDepartment(Long id, int departmentNumber, String departmentName);
}
