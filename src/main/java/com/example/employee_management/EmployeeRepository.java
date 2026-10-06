package com.example.employee_management;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository <Employee, Long>{
    
    public List<Employee> findByManager(Employee manager);
    
}
