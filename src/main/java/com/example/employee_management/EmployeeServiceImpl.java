package com.example.employee_management;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository){
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public Employee createEmployee(Company employer, String firstName, String lastName, String email, LocalDate hireDate, String employeeNumber){
        Employee newEmployee = new Employee(employer, firstName, lastName, email, hireDate);
        newEmployee.setEmployeeNumber(employeeNumber);
        
        return employeeRepository.save(newEmployee);
    }

    @Override
    public Employee getEmployeeInfo(Long id, Company employer){
        Optional<Employee> result = employeeRepository.findByIdAndEmployer(id, employer);

        return result.orElseThrow(() -> 
            new EmployeeNotFoundException("Employee does not exist."));
    }
    @Override
    public Company getCompanyInfo(Long id){
        Optional<Company> result = companyRepository.findById(id);

        return result.orElseThrow(() ->
            new CompanyNotFoundException("Company does not exist."));
    }
    @Override
    public List<Employee> listAllEmployees(Company employer){

        return employeeRepository.findByEmployer(employer);
    }

    @Override
    public void updateSalary(Company employer, Long id, BigDecimal salary){
        Employee employee = findEmployeeByIdAndEmployer(id, employer);
        employee.setSalary(salary);
        employeeRepository.save(employee);
    }

    @Override
    public void updateJobTitle(Company employer, Long id, String jobTitle){
        Employee employee = findEmployeeByIdAndEmployer(id, employer);
        employee.setJobTitle(jobTitle);
        employeeRepository.save(employee);
    }

    @Override
    public void updateDepartment(Company employer, Long id, String departmentCode, String departmentName){
        Employee employee = findEmployeeByIdAndEmployer(id, employer);
        Department department = new Department(departmentCode, departmentName);
        employee.setDepartment(department);
        employeeRepository.save(employee);
    }
    @Override 
    public void updateManager(Company employer, Long id, Long managerId){
        
        if (id.equals(managerId)){
            throw new IllegalArgumentException("Employee cannot be assigned as their own manager.");
        }
        Employee employee = findEmployeeByIdAndEmployer(id, employer);
        Employee manager = findEmployeeByIdAndEmployer(managerId, employer);
        Employee checkManager = manager;
        while (checkManager != null){
            if (checkManager.getId().equals(id)){
                throw new IllegalArgumentException("Submitted ID is already in this employee's reporting chain.");
            }
            checkManager = checkManager.getManager();
        }

        employee.setManager(manager);
        employeeRepository.save(employee);
    }
    @Override
    public List<Employee> listEmployeesReportingToManager(Company employer, Long managerId){
        Employee manager = findEmployeeByIdAndEmployer(managerId, employer);

        return employeeRepository.findByEmployerAndManager(employer, manager);
    }
    @Override
    public List<Employee> allReportsInHierarchy(Company employer, Long managerId){
        List<Employee> employeesInHierarchy = new ArrayList<>();
        Queue<Employee> employeeQueue = new LinkedList<>();
        List<Employee> managerDirectReports = listEmployeesReportingToManager(employer, managerId);
        employeeQueue.addAll(managerDirectReports);
        employeesInHierarchy.addAll(managerDirectReports);

        while (!employeeQueue.isEmpty()){
            Employee nextEEInQueue = employeeQueue.poll();
            List<Employee> returnedEmployees = listEmployeesReportingToManager(employer, nextEEInQueue.getId());
            employeesInHierarchy.addAll(returnedEmployees);
            employeeQueue.addAll(returnedEmployees);
        }

        return employeesInHierarchy;
    }
    public Employee findEmployeeByIdAndEmployer(Long id, Company employer){
        Optional<Employee> result = employeeRepository.findByIdAndEmployer(id, employer);

        return result.orElseThrow(() -> 
            new EmployeeNotFoundException("Employee does not exist.")
        );
    }
    public List<Employee> listEmployeesByEmployer(Company employer){
        return employeeRepository.findByEmployer(employer);
    }
}