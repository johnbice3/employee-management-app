package com.example.employee_management;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository){
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(String firstName, String lastName, String email, LocalDate hireDate){
        Employee newEmployee = new Employee(firstName, lastName, email, hireDate);
        
        return employeeRepository.save(newEmployee);
    }

    @Override
    public Employee getEmployeeInfo(Long id){
        Optional<Employee> result = employeeRepository.findById(id);

        return result.orElseThrow(() -> 
            new EmployeeNotFoundException("That employee does not exist"));
    }
    
    @Override
    public List<Employee> listAllEmployees(){
        return employeeRepository.findAll();
    }

    @Override
    public void updateSalary(Long id, BigDecimal salary){
        Employee employee = getEmployeeInfo(id);
        employee.setSalary(salary);
        employeeRepository.save(employee);
    }

    @Override
    public void updateJobTitle(Long id, String jobTitle){
        Employee employee = getEmployeeInfo(id);
        employee.setJobTitle(jobTitle);
        employeeRepository.save(employee);
    }

    @Override
    public void updateDepartment(Long id, String departmentCode, String departmentName){
        Employee employee = getEmployeeInfo(id);
        Department department = new Department(departmentCode, departmentName);
        employee.setDepartment(department);
        employeeRepository.save(employee);
    }
}
