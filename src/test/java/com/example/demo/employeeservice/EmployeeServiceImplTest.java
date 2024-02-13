package com.example.demo.employeeservice;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.demo.entity.Employee;
import com.example.demo.entity.QuestionTest;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repo.EmployeeRepo;
import com.example.demo.repo.QuestionTestRepo;
import com.example.demo.service.EmployeeServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)

public class EmployeeServiceImplTest {
	@Mock
	private EmployeeRepo employeeRepo;

	@InjectMocks
	private EmployeeServiceImpl employeeService;

	@Mock
	QuestionTestRepo questionTestRepo;

	@Test
	public void testSaveEmployee_Success() {
		Employee employee = new Employee();
		employee.setFirstName("John");
		employee.setLastName("Doe");
		employee.setEmail("abc@gmail.com");
		employee.setEmployee_id(1L);
		employee.setPassword("admin");
		when(employeeRepo.save(any(Employee.class))).thenReturn(new Employee());
		employeeService.save(employee);

		verify(employeeRepo, times(1)).save(employee);
	}

	@Test
	public void testSaveEmployeeFailure() {
		// Arrange
		Employee employeeToSave = new Employee();
		employeeToSave.setFirstName("John");
		employeeToSave.setLastName("Doe");

		when(employeeRepo.save(employeeToSave)).thenThrow(EmployeeNotFoundException.class);

		assertThrows(EmployeeNotFoundException.class, () -> {
			employeeService.save(employeeToSave);
		});
	}

	@Test
	public void testFindAllEmployees() {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		when(employeeRepo.findAll()).thenReturn(employees);
		List<Employee> foundEmployees = employeeService.findAll();
		assertEquals(employees.size(), foundEmployees.size());
	}

//
	@Test
	public void testFindEmployeeById() {
		Employee employee = new Employee();
		employee.setEmployee_id(1L);
		when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
		Employee foundEmployee = employeeService.findById(1L);
		assertEquals(1L, foundEmployee.getEmployee_id());
	}


	@Test
	    public void testFindEmployeeById_NotFound() {
	        when(employeeRepo.findById(1L)).thenReturn(Optional.empty());
	        assertThrows(ServiceException.class, () -> {
	            employeeService.findById(1L);
	        });
	    }

	@Test
	    public void testDeleteEmployee() {
	        when(employeeRepo.findById(1L)).thenReturn(Optional.of(new Employee()));
	        String result = employeeService.delete(1L);
	        assertEquals("Employee id 1 deleted successfully", result);
	    }

	@Test
	    public void testDeleteEmployeeNotFound() {
	        when(employeeRepo.findById(1L)).thenReturn(Optional.empty());
	        String result = employeeService.delete(1L);
	        assertEquals("id is not present", result);
	    }

	@Test
	public void testUpdateEmployee_Success() {
		Employee employee = new Employee();
		employee.setEmployee_id(1L);
		when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
		when(employeeRepo.save(any(Employee.class))).thenReturn(employee);
		Employee updatedEmployee = employeeService.update(employee);
		assertEquals(1L, updatedEmployee.getEmployee_id());
	}

	@Test
	public void testUpdateEmployeeNotFound() {
		Employee employee = new Employee();
		employee.setEmployee_id(1L);
		when(employeeRepo.findById(1L)).thenReturn(Optional.empty());
		assertThrows(ServiceException.class, () -> {
			employeeService.update(employee);
		});
	}

	@Test
	public void testAssignTestToEmployee_Success() {
		Employee employee = new Employee();
		employee.setEmployee_id(1L);
		QuestionTest test = new QuestionTest();
		test.setTestId(1L);
		when(employeeRepo.findById(1L)).thenReturn(Optional.of(employee));
		when(questionTestRepo.findById(1L)).thenReturn(Optional.of(test));

		assertDoesNotThrow(() -> {
			employeeService.assignTestToEmployee(1L, 1L);
		});
	}

	@Test
	    public void testAssignTestToEmployeeEmployeeNotFound() {
	        when(employeeRepo.findById(1L)).thenReturn(Optional.empty());
	        assertThrows(ServiceException.class, () -> {
	            employeeService.assignTestToEmployee(1L, 1L);
	        });
	    }

	@Test
	    public void testAssignTestToEmployeeTestNotFound() {
	        when(employeeRepo.findById(1L)).thenReturn(Optional.of(new Employee()));
	        when(questionTestRepo.findById(1L)).thenReturn(Optional.empty());
	        assertThrows(ServiceException.class, () -> {
	            employeeService.assignTestToEmployee(1L, 1L);
	        });
	    }

	@Test
	    public void testGetAllAssignedTestsEmployeeNotFound() {
	        when(employeeRepo.findById(1L)).thenReturn(Optional.empty());
	        assertThrows(EntityNotFoundException.class, () -> {
	            employeeService.getAllAssignedTests(1L);
	        });
	    }
}
