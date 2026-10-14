package com.example.employee_management;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository <Employee, Long>{
    
    public List<Employee> findByManager(Employee manager);
    
    public List<Employee> findByEmployer(Company employer);

    public Optional<Employee> findByIdAndEmployer(Long id, Company employer);
}
