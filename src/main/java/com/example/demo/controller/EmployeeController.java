package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.Employee;
import com.example.demo.exception.CategoryNotFoundException;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;

import io.swagger.models.HttpMethod;

@RestController
@RequestMapping(value = "api/v1/employees")
public class EmployeeController {

	private static final Logger log = (Logger) LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	EmployeeService employeeService;
	
	@Autowired
    private RestTemplate restTemplate;

	@PostMapping
	public Employee saveData(@RequestBody Employee employee) {
		Employee emp = new Employee();
		try {
			emp = employeeService.save(employee);
			log.info("Category Request:{}", emp);
		} catch (EmployeeNotFoundException e) {
			log.error("Exception in save data in EmployeeController" + e.getMessage());
		}
		return emp;
	}


	@GetMapping
	public List<Employee> findAllEmployee() {
		List<Employee> employee = new ArrayList<>();
		try {
			employee = employeeService.findAll();
			log.info("Getting all employees");

		} catch (EmployeeNotFoundException e) {
			log.info("Exception in finaAll data in employeeService" + e.getMessage());

		}

		return employee;
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> findByIdEmployee(@PathVariable("id") Long id) {
		try {
			if (id != null) {
				Employee employee = employeeService.findById(id);
				log.info("Getting with id employee");
				return ResponseEntity.ok(employee);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (EmployeeNotFoundException e) {
			log.error("Exception in findById data in EmployeeController: " + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Employee id not found");
		}
	}

	@PutMapping
	public ResponseEntity<?> updateEmployee(@RequestBody Employee employee) {
		try {
			if (employee.getEmployee_id() == null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("employee ID is required for update");
			}
			log.info("Update employee Request: {}", employee);
			Employee employeeUpdate = employeeService.update(employee);
			return ResponseEntity.ok(employeeUpdate);
		} catch (CategoryNotFoundException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("employee not found");

		}
	}
//
	@DeleteMapping("/{id}")
	public String deleteCategory(@PathVariable("id") Long id) {
		try {
			return employeeService.delete(id);
		} catch (EmployeeNotFoundException e) {
			log.error("Error deleting employee with ID {}: {}", id, e.getMessage(), e);
			return "Failed to delete employee. Check logs for details.";
		}
	}

	 @PostMapping("/{employeeId}/assign-test/{testId}")
	    public ResponseEntity<String> assignTestToEmployee(@PathVariable Long employeeId, @PathVariable Long testId) {
	        // Call the Test Management Module to get the test details
	        ResponseEntity<Employee> responseEntity = restTemplate.exchange("http://test-management-service/tests/{testId}",
	                HttpMethod.GET, null, Employee.class, testId);

	        if (responseEntity.getStatusCode() == HttpStatus.OK) {
	        	Employee test = responseEntity.getBody();
	            // Assign the test to the employee
	            employeeService.assignTest(employeeId, test);
	            return ResponseEntity.ok("Test assigned to employee successfully.");
	        } else {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error assigning test to employee.");
	        }
	    }
}
