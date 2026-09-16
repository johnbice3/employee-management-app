package com.example.employee_management;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

@RestController
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

}
