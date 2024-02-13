package com.example.demo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.demo.entity.AssignTestDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.QuestionTest;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;

import jakarta.persistence.EntityNotFoundException;

public class EmployeeControllerTest {

	private static final Object OK = null;

	private static final Object NOT_FOUND = null;

	@Mock
	private EmployeeService employeeService;

	@InjectMocks
	private EmployeeController employeeController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testSaveData() {

		Employee employee = new Employee();
		employee.setFirstName("gita");
		employee.setLastName("patil");
		employee.setEmail("abc@gmail.com");
		employee.setEmployee_id(1L);
		when(employeeService.save(any(Employee.class))).thenReturn(employee);

		Employee savedEmployee = employeeController.saveData(employee);

		assertEquals(1L, savedEmployee.getEmployee_id());
	}

	@Test
	public void testSaveDataException() {

		Employee employee = new Employee();
		employee.setFirstName("gita");
		employee.setLastName("patil");
		employee.setEmail("abc@gmail.com");
		employee.setEmployee_id(1L);
		when(employeeService.save(any(Employee.class))).thenThrow(new EmployeeNotFoundException("Employee not found"));

		Employee savedEmployee = employeeController.saveData(employee);

	}

	@Test
	public void testFindAllEmployee() {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee());
		when(employeeService.findAll()).thenReturn(employees);
		List<Employee> foundEmployees = employeeController.findAllEmployee();
		assertEquals(1, foundEmployees.size());
	}

	@Test
	public void testFindAllEmployeeException() {
		List<Employee> employees = null;
		when(employeeService.findAll()).thenThrow(new EmployeeNotFoundException("Employee not found"));
		List<Employee> foundEmployees = employeeController.findAllEmployee();
		assertNotNull(foundEmployees);
	}

	@Test
	public void testFindByIdEmployee() {
		Employee employee = new Employee();
		employee.setEmployee_id(1L);
		when(employeeService.findById(1L)).thenReturn(employee);
		ResponseEntity<?> responseEntity = employeeController.findByIdEmployee(1L);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(employee, responseEntity.getBody());
	}

	@Test
	    public void testFindByIdEmployeeException() {
	        when(employeeService.findById(2L)).thenThrow(new EmployeeNotFoundException("Employee not found"));
	        ResponseEntity<?> responseEntityNotFound = employeeController.findByIdEmployee(2L);
	        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntityNotFound.getStatusCode());
	        assertEquals("Employee id not found", responseEntityNotFound.getBody());
	    }

	@Test
	public void testUpdateEmployee() {
		List<QuestionTest> question = new ArrayList<>();
		Employee employee = new Employee();
		employee.setFirstName("gita");
		employee.setLastName("patil");
		employee.setEmail("abc@gmail.com");
		employee.setEmployee_id(1L);
		employee.setPassword("admin");
		employee.setTests(question);
		when(employeeService.update(any(Employee.class))).thenReturn(employee);
		employeeController.updateEmployee(new Employee());

	}

	@Test
	public void testUpdateEmployeeException() {
		List<QuestionTest> question = new ArrayList<>();
		Employee employee = new Employee();
		employee.setFirstName("gita");
		employee.setLastName("patil");
		employee.setEmail("abc@gmail.com");
		employee.setEmployee_id(1L);
		employee.setPassword("admin");
		employee.setTests(question);
		when(employeeService.update(any(Employee.class)))
				.thenThrow(new EmployeeNotFoundException("employee ID is required for update"));
		ResponseEntity<?> responseEntityError = employeeController.updateEmployee(new Employee());
		assertEquals(HttpStatus.BAD_REQUEST, responseEntityError.getStatusCode());
		assertEquals("employee ID is required for update", responseEntityError.getBody());

	}

	@Test
	    public void testDeleteEmployee() {
	        when(employeeService.delete(1L)).thenReturn("Deleted");
	        String result = employeeController.deleteEmployee(1L);

	        assertEquals("Deleted", result);
	    }

//	@Test
//	    public void testDeleteEmployeeException() {
//		try {
//	    	when(employeeService.delete(2L)).thenThrow(new EmployeeNotFoundException("Employee not found"));
//	    	String resultNotFound = employeeController.deleteEmployee(2L);
//	    	assertEquals("Failed to delete employee. Check logs for details.", resultNotFound);
//	    }catch (EmployeeNotFoundException e) {
//			e.getMessage();
//		}
//	}

	@Test
	public void testAssignTestToEmployee_Success() {
		// Prepare test data
		AssignTestDto assignTestDto = new AssignTestDto();
		assignTestDto.setEmployee_id(1L);
		assignTestDto.setTest_id(100L);

		doNothing().when(employeeService).assignTestToEmployee(assignTestDto.getEmployee_id(),
				assignTestDto.getTest_id());
		ResponseEntity<AssignTestDto> responseEntity = employeeController.assignTestToEmployee(assignTestDto);
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals(assignTestDto, responseEntity.getBody());
	}

	@Test
	public void testAssignTestToEmployeeEntityNotFoundException() {
		AssignTestDto assignTestDto = new AssignTestDto();
		assignTestDto.setEmployee_id(1L);
		assignTestDto.setTest_id(100L);
		doThrow(EntityNotFoundException.class).when(employeeService)
				.assignTestToEmployee(assignTestDto.getEmployee_id(), assignTestDto.getTest_id());
		ResponseEntity<AssignTestDto> responseEntity = employeeController.assignTestToEmployee(assignTestDto);
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
	}

	@Test
	public void testGetAllAssignedTests_Success() {
		EmployeeService employeeServiceMock = mock(EmployeeService.class);
		List<QuestionTest> assignedTests = new ArrayList<>();
		when(employeeServiceMock.getAllAssignedTests(1L)).thenReturn(assignedTests);

		ResponseEntity<List<QuestionTest>> responseEntity = employeeController.getAllAssignedTests(1L);

		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertNotNull(responseEntity.getBody());
		assertEquals(assignedTests, responseEntity.getBody());
	}

	@Test
	public void testGetAllAssignedTests_EntityNotFoundException() {

		EmployeeService employeeServiceMock = mock(EmployeeService.class);
		when(employeeServiceMock.getAllAssignedTests(1L)).thenThrow(EntityNotFoundException.class);
		ResponseEntity<List<QuestionTest>> responseEntity = employeeController.getAllAssignedTests(1L);
		assertTrue(responseEntity.getBody() == null || responseEntity.getBody().isEmpty());
	}

}
