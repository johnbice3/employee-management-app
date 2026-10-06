package com.example.employee_management;
import java.util.List;

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

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable Long id){
        return employeeService.getEmployeeInfo(id);
    } 

    @PostMapping
    public Employee createEmployeeByRequest(@RequestBody @Valid CreateEmployeeRequest request){
        return employeeService.createEmployee(request.getFirstName(), request.getLastName(), request.getEmail(), request.getHireDate());
    }
    @PutMapping ("/{id}/salary")
    public ResponseEntity<String> salaryUpdateRequest(@PathVariable Long id, @RequestBody @Valid UpdateSalaryRequest request){
        employeeService.updateSalary(id, request.getSalary());

        return ResponseEntity.ok("Salary updated successfully");
    }
    @PutMapping ("/{id}/jobTitle")
    public ResponseEntity<String> jobTitleUpdateRequest(@PathVariable Long id, @RequestBody @Valid UpdateJobTitleRequest request){
        employeeService.updateJobTitle(id, request.getJobTitle());

        return ResponseEntity.ok("Job Title updated successfully");
    }
    @PutMapping ("/{id}/department")
    public ResponseEntity<String> departmentUpdateRequest(@PathVariable Long id, @RequestBody @Valid UpdateDepartmentRequest request){
        employeeService.updateDepartment(id, request.getDepartmentCode(), request.getDepartmentName());

        return ResponseEntity.ok("Department updated successfully");
    }
    @GetMapping
    public List<Employee> listAllEmployees(){
        return employeeService.listAllEmployees();
    }
    @PutMapping("/{id}/manager")
    public ResponseEntity<String> managerUpdateRequest(@PathVariable Long id, @RequestBody @Valid UpdateManagerRequest request) {
        employeeService.updateManager(id, request.getManagerId());

        return ResponseEntity.ok("Manager updated successfully");
    }
    @GetMapping("/{managerId}/direct-reports")
    public List<Employee> listEmployeesReportToManager(@PathVariable Long managerId){
        return employeeService.listEmployeesReportingToManager(managerId);
    }
    
}
