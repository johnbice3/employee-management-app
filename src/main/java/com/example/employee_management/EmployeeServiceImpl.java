package com.example.employee_management;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    public Employee createEmployee(String firstName, String lastName, String email, LocalDate hireDate){
        Employee newEmployee = new Employee(firstName, lastName, email, hireDate);
        
        return employeeRepository.save(newEmployee);
    }

    public Employee getEmployeeInfo(Long id){
        Optional<Employee> result = employeeRepository.findById(id);

        return result.orElseThrow(() -> 
            new EmployeeNotFoundException("That employee does not exist"));
    }

    public List<Employee> listAllEmployees(){
        return employeeRepository.findAll();
    }

    public void updateSalary(Long id, BigDecimal salary){
        Employee employee = getEmployeeInfo(id);
        employee.setSalary(salary);
        employeeRepository.save(employee);
    }

    public void updateJobTitle(Long id, String jobTitle){
        Employee employee = getEmployeeInfo(id);
        employee.setJobTitle(jobTitle);
        employeeRepository.save(employee);
    }

    public void updateDepartment(Long id, int departmentNumber, String departmentName){
        Employee employee = getEmployeeInfo(id);
        employee.setDepartment(departmentNumber, departmentName);
        employeeRepository.save(employee);
    }
}
