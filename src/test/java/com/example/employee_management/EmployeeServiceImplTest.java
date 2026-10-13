package com.example.employee_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import java.util.Optional;
import java.util.List;
import java.time.LocalDate;
import java.lang.reflect.Field;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
    private Company testCompany;

    @Mock 
    EmployeeRepository testRepository;
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
}
