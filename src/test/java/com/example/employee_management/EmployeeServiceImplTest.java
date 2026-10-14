package com.example.employee_management;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
    Company testCompany = new Company("Test Company");

    @Mock 
    EmployeeRepository testRepository;
    @Mock
    CompanyRepository testCompanyRepository;
    @InjectMocks 
    EmployeeServiceImpl testServiceImpl;

    @Test
    void shouldThrowExceptionForEmployeeManagingSelf(){
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> testServiceImpl.updateManager(2L,2L)
        );

        assertEquals("Employee cannot be assigned as their own manager.", exception.getMessage());

    }

    @Test
    void shouldThrowIllegalExceptionForCircularManagerChain() throws NoSuchFieldException, IllegalAccessException {
        Employee firstTestEmployee = new Employee(testCompany, "Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));
        Employee secondTestEmployee = new Employee(testCompany, "Jayce", "Bice", "jaycebice@test.com", LocalDate.of(2026, 10, 12));
        
        Field idField = Employee.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(firstTestEmployee, 1L);
        idField.set(secondTestEmployee, 2L);

        secondTestEmployee.setManager(firstTestEmployee);

        when(testRepository.findById(1L))
            .thenReturn(Optional.of(firstTestEmployee));
        when(testRepository.findById(2L))
            .thenReturn(Optional.of(secondTestEmployee));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class, 
            () -> testServiceImpl.updateManager(1L, 2L)
        );

        assertEquals("Submitted ID is already in this employee's reporting chain.", exception.getMessage());
    }

    @Test
    void shouldSuccessfullyUpdateManagerIfValid() throws NoSuchFieldException, IllegalAccessException{
        Employee firstTestEmployee = new Employee(testCompany, "Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));
        Employee secondTestEmployee = new Employee(testCompany, "Jayce", "Bice", "jaycebice@test.com", LocalDate.of(2026, 10, 12));
        
        Field idField = Employee.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(firstTestEmployee, 1L);
        idField.set(secondTestEmployee, 2L);

        when(testRepository.findById(1L))
            .thenReturn(Optional.of(firstTestEmployee));
        when(testRepository.findById(2L))
            .thenReturn(Optional.of(secondTestEmployee));

        testServiceImpl.updateManager(2L, 1L);

        assertEquals(firstTestEmployee, secondTestEmployee.getManager());
        verify(testRepository).save(secondTestEmployee);
    }

    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenEmployeeDoesNotExist() {
        EmployeeNotFoundException exception = assertThrows(
            EmployeeNotFoundException.class, 
            () -> testServiceImpl.updateManager(1L, 2L)
        );

        assertEquals("That employee does not exist", exception.getMessage());
    }

    @Test
    void shouldThrowEmployeeNotFoundExceptionWhenManagerIdDoesNotExist() throws NoSuchFieldException, IllegalAccessException{
        Employee firstTestEmployee = new Employee(testCompany, "Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));
        
        Field idField = Employee.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(firstTestEmployee, 1L);

        when(testRepository.findById(1L))
            .thenReturn(Optional.of(firstTestEmployee));
        when(testRepository.findById(2L))
            .thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(
            EmployeeNotFoundException.class,
            () -> testServiceImpl.updateManager(1L, 2L)
        );

        assertEquals("That employee does not exist", exception.getMessage());
    }
    
    @Test
    void shouldReturnDirectAndIndirectReportsInHierarchy() throws NoSuchFieldException, IllegalAccessException{
        Employee firstTestEmployee = new Employee(testCompany, "Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));
        Employee secondTestEmployee = new Employee(testCompany, "Jayce", "Bice", "jaycebice@test.com", LocalDate.of(2026, 10, 12));
        Employee thirdTestEmployee = new Employee(testCompany, "Cheddar", "Bice", "cheddarbice@test.com", LocalDate.of(2026, 10, 12));

        Field idField = Employee.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(firstTestEmployee, 1L);
        idField.set(secondTestEmployee, 2L);
        idField.set(thirdTestEmployee, 3L);

        secondTestEmployee.setManager(firstTestEmployee);
        thirdTestEmployee.setManager(secondTestEmployee);

        when(testRepository.findById(1L))
            .thenReturn(Optional.of(firstTestEmployee));
        when(testRepository.findById(2L))
            .thenReturn(Optional.of(secondTestEmployee));
        when(testRepository.findById(3L))
            .thenReturn(Optional.of(thirdTestEmployee));    
        when(testRepository.findByManager(firstTestEmployee))
            .thenReturn(List.of(secondTestEmployee));
        when(testRepository.findByManager(secondTestEmployee))
            .thenReturn(List.of(thirdTestEmployee));
        when(testRepository.findByManager(thirdTestEmployee))
            .thenReturn(List.of());
        
        List<Employee> firstEmployeeHierarchy = testServiceImpl.allReportsInHierarchy(1L);

        assertEquals(2, firstEmployeeHierarchy.size());
        assertTrue(firstEmployeeHierarchy.contains(secondTestEmployee));
        assertTrue(firstEmployeeHierarchy.contains(thirdTestEmployee));
    }

    @Test
    void shouldReturnEmptyListForManagerWithNoDirectReports() throws NoSuchFieldException, IllegalAccessException{
        Employee firstTestEmployee = new Employee(testCompany, "Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));

        Field idField = Employee.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(firstTestEmployee, 1L);

        when(testRepository.findById(1L))
            .thenReturn(Optional.of(firstTestEmployee));
        when(testRepository.findByManager(firstTestEmployee))
            .thenReturn(List.of());   

        List<Employee> firstEmployeeHierarchy = testServiceImpl.allReportsInHierarchy(1L);

        assertTrue(firstEmployeeHierarchy.isEmpty());
    }

    @Test
    void shouldReturnAllDirectAndIndirectReportsInHierarchy() throws NoSuchFieldException, IllegalAccessException{
        Employee firstTestEmployee = new Employee(testCompany, "Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));
        Employee secondTestEmployee = new Employee(testCompany, "Jayce", "Bice", "jaycebice@test.com", LocalDate.of(2026, 10, 12));
        Employee thirdTestEmployee = new Employee(testCompany, "Cheddar", "Bice", "cheddarbice@test.com", LocalDate.of(2026, 10, 12));
        Employee fourthTestEmployee = new Employee(testCompany, "John", "Bice", "johnbice@test.com", LocalDate.of(2026, 10, 12));

        Field idField = Employee.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(firstTestEmployee, 1L);
        idField.set(secondTestEmployee, 2L);
        idField.set(thirdTestEmployee, 3L);
        idField.set(fourthTestEmployee, 4L);

        secondTestEmployee.setManager(firstTestEmployee);
        thirdTestEmployee.setManager(secondTestEmployee);
        fourthTestEmployee.setManager(firstTestEmployee);

        when(testRepository.findById(1L))
            .thenReturn(Optional.of(firstTestEmployee));
        when(testRepository.findById(2L))
            .thenReturn(Optional.of(secondTestEmployee));
        when(testRepository.findById(3L))
            .thenReturn(Optional.of(thirdTestEmployee));    
        when(testRepository.findById(4L))
            .thenReturn(Optional.of(fourthTestEmployee)); 
        when(testRepository.findByManager(firstTestEmployee))
            .thenReturn(List.of(secondTestEmployee, fourthTestEmployee));
        when(testRepository.findByManager(secondTestEmployee))
            .thenReturn(List.of(thirdTestEmployee));
        when(testRepository.findByManager(thirdTestEmployee))
            .thenReturn(List.of());
        when(testRepository.findByManager(fourthTestEmployee))
            .thenReturn(List.of());
        
        List<Employee> firstEmployeeHierarchy = testServiceImpl.allReportsInHierarchy(1L);

        assertEquals(3, firstEmployeeHierarchy.size());
        assertTrue(firstEmployeeHierarchy.contains(secondTestEmployee));
        assertTrue(firstEmployeeHierarchy.contains(thirdTestEmployee));
        assertTrue(firstEmployeeHierarchy.contains(fourthTestEmployee));
    }
    @Test 
    void shouldTrimWhitespaceFromEmployeeNumber(){
        Employee testEmployee = new Employee(testCompany, "Jack", "Bice", "johnbice3@test.com", LocalDate.of(2026, 10, 14));
        Employee secondTestEmployee = new Employee(testCompany, "John", "Bice", "johnbice3@test.com", LocalDate.of(2026, 10, 14));
        Employee thirdTestEmployee = new Employee(testCompany, "Jayce", "Bice", "johnbice3@test.com", LocalDate.of(2026, 10, 14));
        testEmployee.setEmployeeNumber("  00125   ");
        secondTestEmployee.setEmployeeNumber("");
        thirdTestEmployee.setEmployeeNumber(null);

        assertEquals("00125", testEmployee.getEmployeeNumber());
        assertNull(null, secondTestEmployee.getEmployeeNumber());
        assertNull(null, thirdTestEmployee.getEmployeeNumber());

    }
    @Test
    void shouldReturnEmployeeBelongingToEmployer() throws NoSuchFieldException, IllegalAccessException{
        Employee testEmployee = new Employee(testCompany, "Jack", "Bice", "johnbice3@test.com", LocalDate.of(2026, 10, 14));
        
        Field idField = Employee.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(testEmployee, 1L);
        when(testRepository.findByIdAndEmployer(1L, testCompany))
            .thenReturn(Optional.of(testEmployee));

        assertEquals(testEmployee,testServiceImpl.findEmployeeByIdAndEmployer(1L, testCompany));
    }
    @Test 
    void shouldThrowExceptionWhenEmployeeDoesNotExistInCompany(){
        EmployeeNotFoundException exception = assertThrows(
            EmployeeNotFoundException.class,
            () -> testServiceImpl.findEmployeeByIdAndEmployer(1L, testCompany)
        );
        assertEquals("Employee does not exist.", exception.getMessage());
    }
    @Test
    void shouldReturnAllEmployeesForProvidedEmployer(){
        Employee testEmployee = new Employee(testCompany, "Jack", "Bice", "jackbice3@test.com", LocalDate.of(2026, 10, 14));
        Employee secondTestEmployee = new Employee(testCompany, "John", "Bice", "johnbice3@test.com", LocalDate.of(2026, 10, 14));

        when (testRepository.findByEmployer(testCompany))
            .thenReturn(List.of(testEmployee, secondTestEmployee)
        );

        List<Employee> expectedListOfEmployees = List.of(testEmployee, secondTestEmployee);
        
        List<Employee> employeesInCompany = testServiceImpl.listEmployeesByEmployer(testCompany);

        assertEquals(expectedListOfEmployees, employeesInCompany);   
    }
    @Test 
    void shouldReturnValidCompanyInfo() throws NoSuchFieldException, IllegalAccessException{
        Field idField = Company.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(testCompany, 1L);

        when (testCompanyRepository.findById(1L))
            .thenReturn(Optional.of(testCompany));
        
        assertEquals(testCompany, testServiceImpl.getCompanyInfo(1L));
    }
    @Test 
    void shouldThrowExceptionWhenCompanyDoesNotExist(){
        CompanyNotFoundException exception = assertThrows(
            CompanyNotFoundException.class, 
            () -> testServiceImpl.getCompanyInfo(1L)
        );

        assertEquals("That company does not exist", exception.getMessage());
    }
}
