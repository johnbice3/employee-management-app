package com.example.employee_management;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public Employee createEmployeeByRequest(@RequestBody CreateEmployeeRequest request){
        return employeeService.createEmployee(request.getFirstName(), request.getLastName(), request.getEmail());
    }
}
