package com.example.employee_management;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {
    
    public Employee createEmployee(Company employer, String firstName, String lastName, String email, LocalDate hireDate, String employeeNumber);

    public Employee getEmployeeInfo(Long id, Company employer);

    public Company getCompanyInfo(Long id);

    public List<Employee> listAllEmployees(Company employer);

    public void updateSalary(Company employer, Long id, BigDecimal salary);

    public void updateJobTitle(Company employer, Long id, String jobTitle);

    public void updateDepartment(Company employer, Long id, String departmentCode, String departmentName);

    public void updateManager(Company employer, Long id, Long managerId);

    public List<Employee> listEmployeesReportingToManager(Company employer, Long managerId);

    public List<Employee> allReportsInHierarchy(Company employer, Long managerId);
}
