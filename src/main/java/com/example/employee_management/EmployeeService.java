package com.example.employee_management;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    
    public Employee createEmployee(Company employer, String firstName, String lastName, String email, LocalDate hireDate, String employeeNumber);

    public Employee getEmployeeInfo(Long id);

    public Company getCompanyInfo(Long id);

    public List<Employee> listAllEmployees();

    public void updateSalary(Company employer, Long id, BigDecimal salary);

    public void updateJobTitle(Long id, String jobTitle);

    public void updateDepartment(Long id, String departmentCode, String departmentName);

    public void updateManager(Long id, Long managerId);

    public List<Employee> listEmployeesReportingToManager(Long managerId);

    public List<Employee> allReportsInHierarchy(Long managerId);
}
