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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.AssignTestDto;
import com.example.demo.entity.Employee;
import com.example.demo.entity.QuestionTest;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.service.EmployeeService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "api/v1/employees")
public class EmployeeController {

	private static final Logger log = (Logger) LoggerFactory.getLogger(EmployeeController.class);

	@Autowired
	EmployeeService employeeService;

	@PostMapping
	public ResponseEntity<Employee> registerEmployee(@RequestBody Employee employee) {
		Employee emp = new Employee();
		try {
			emp = employeeService.register(employee);
			log.info("Employee Request:{}", emp);

		} catch (EmployeeNotFoundException e) {
			log.error("Exception in save data in EmployeeController " + e.getMessage());
		}
        return ResponseEntity.ok(emp);
		
	}
	
	
	@PostMapping("/login")
    public ResponseEntity<String> loginEmployee(@RequestParam("email") String email, @RequestParam("password") String password) {
		 if (employeeService.login(email, password)) {
	            return ResponseEntity.ok("Login successful");
	        } else {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	        }
	      
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
	public ResponseEntity<?> findByIdEmployee(@PathVariable("idi") Long id) {
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
		} catch (EmployeeNotFoundException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("employee not found");

		}
	}


	@DeleteMapping("/{id}")
	public String deleteEmployee(@PathVariable("id") Long id) {
		try {
			return employeeService.delete(id);
		} catch (EmployeeNotFoundException e) {
			log.error("Error deleting employee with ID {}: {}", id, e.getMessage(), e);
			return "Failed to delete employee. Check logs for details.";
		}
	}

	@PostMapping("/assign-test")
	public ResponseEntity<AssignTestDto> assignTestToEmployee(@RequestBody AssignTestDto assignTestDto) {
		try {

			employeeService.assignTestToEmployee(assignTestDto.getEmployee_id(), assignTestDto.getTest_id());
			AssignTestDto responseDto = new AssignTestDto();
			responseDto.setEmployee_id(assignTestDto.getEmployee_id());
			responseDto.setTest_id(assignTestDto.getTest_id());
			return ResponseEntity.ok(assignTestDto);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping("/assigned-tests/{employeeId}")
    public ResponseEntity<List<QuestionTest>> getAllAssignedTests(@PathVariable Long employeeId) {
        try {
            List<QuestionTest> assignedTests = employeeService.getAllAssignedTests(employeeId);
            return ResponseEntity.ok(assignedTests);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
