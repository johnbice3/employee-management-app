package com.example.employee_management;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    
    public Employee createEmployee(String firstName, String lastName, String email, LocalDate hireDate);

    public Employee getEmployeeInfo(Long id);

    public List<Employee> listAllEmployees();

    public void updateSalary(Long id, BigDecimal salary);

    public void updateJobTitle(Long id, String jobTitle);

    public void updateDepartment(Long id, String departmentCode, String departmentName);

    public void updateManager(Long id, Long managerId);

    public List<Employee> listEmployeesReportingToManager(Long managerId);
}
