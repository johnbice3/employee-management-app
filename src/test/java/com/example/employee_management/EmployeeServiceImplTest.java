package com.example.employee_management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import java.util.Optional;
import java.time.LocalDate;
import java.lang.reflect.Field;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
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
        Employee firstTestEmployee = new Employee("Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));
        Employee secondTestEmployee = new Employee("Jayce", "Bice", "jaycebice@test.com", LocalDate.of(2026, 10, 12));
        
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
        Employee firstTestEmployee = new Employee("Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));
        Employee secondTestEmployee = new Employee("Jayce", "Bice", "jaycebice@test.com", LocalDate.of(2026, 10, 12));
        
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
        Employee firstTestEmployee = new Employee("Jack", "Bice", "jackbice@test.com", LocalDate.of(2026, 10, 12));
        
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
    
}
