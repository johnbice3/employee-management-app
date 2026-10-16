package com.example.employee_management;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@CrossOrigin
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    @Value("${app.development-company-id}")
    private Long developmentCompanyId;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable Long id){
        Company employer = employeeService.getCompanyInfo(developmentCompanyId);
        return employeeService.getEmployeeInfo(id, employer);
    } 
    @PostMapping
    public Employee createEmployeeByRequest(@RequestBody @Valid CreateEmployeeRequest request){
        return employeeService.createEmployee(request.getEmployer(), request.getFirstName(), request.getLastName(), request.getEmail(), request.getHireDate(), request.getEmployeeNumber());
    }
    @PutMapping ("/{id}/salary")
    public ResponseEntity<String> salaryUpdateRequest(@PathVariable Long id, @RequestBody @Valid UpdateSalaryRequest request){
        Company employer = employeeService.getCompanyInfo(developmentCompanyId);
        employeeService.updateSalary(employer, id, request.getSalary());

        return ResponseEntity.ok("Salary updated successfully");
    }
    @PutMapping ("/{id}/jobTitle")
    public ResponseEntity<String> jobTitleUpdateRequest(@PathVariable Long id, @RequestBody @Valid UpdateJobTitleRequest request){
        Company employer = employeeService.getCompanyInfo(developmentCompanyId);
        employeeService.updateJobTitle(employer, id, request.getJobTitle());

        return ResponseEntity.ok("Job Title updated successfully");
    }
    @PutMapping ("/{id}/department")
    public ResponseEntity<String> departmentUpdateRequest(@PathVariable Long id, @RequestBody @Valid UpdateDepartmentRequest request){
        Company employer = employeeService.getCompanyInfo(developmentCompanyId);
        employeeService.updateDepartment(employer, id, request.getDepartmentCode(), request.getDepartmentName());

        return ResponseEntity.ok("Department updated successfully");
    }
    @GetMapping
    public List<Employee> listAllEmployees(){
        Company employer = employeeService.getCompanyInfo(developmentCompanyId);

        return employeeService.listAllEmployees(employer);
    }
    @PutMapping("/{id}/manager")
    public ResponseEntity<String> managerUpdateRequest(@PathVariable Long id, @RequestBody @Valid UpdateManagerRequest request) {
        Company employer = employeeService.getCompanyInfo(developmentCompanyId);
        employeeService.updateManager(employer, id, request.getManagerId());

        return ResponseEntity.ok("Manager updated successfully");
    }
    @GetMapping("/{managerId}/direct-reports")
    public List<Employee> listEmployeesReportToManager(@PathVariable Long managerId){
        Company employer = employeeService.getCompanyInfo(developmentCompanyId);
        return employeeService.listEmployeesReportingToManager(employer, managerId);
    }
    @GetMapping("/{managerId}/all-reports")
    public List<Employee> listAllEmployeesInHierarchy(@PathVariable Long managerId){
        Company employer = employeeService.getCompanyInfo(developmentCompanyId);
        return employeeService.allReportsInHierarchy(employer, managerId);
    } 
}
