package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.AssignTest;
import com.example.demo.entity.AssignTestResponse;
import com.example.demo.entity.Employee;
import com.example.demo.entity.QuestionTest;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repo.AssignTestRepo;
import com.example.demo.repo.EmployeeRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	AssignTestRepo assignTestRepo;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Employee register(Employee employee) {
		try {
			return employeeRepo.save(employee);
		} catch (EmployeeNotFoundException e) {
			throw new EmployeeNotFoundException("Error saving employee: " + e.getMessage());
		}
	}

	@Override
	public List<Employee> findAll() {
		try {
			return employeeRepo.findAll();
		} catch (EmployeeNotFoundException e) {
			throw new EmployeeNotFoundException("Error saving Employee: " + e.getMessage());
		}
	}

	@Override
	public Employee findById(Long id) {
		try {
			Optional<Employee> employee = employeeRepo.findById(id);
			if (employee.isPresent()) {
				return employeeRepo.findById(id).get();
			} else {
				throw new EmployeeNotFoundException("Employee not found for ID: " + id);

			}

		} catch (Exception e) {
			throw new ServiceException("Error retrieving employeeId: " + e.getMessage(), e);

		}
	}

	@Override
	public String delete(Long id) {
		Optional<Employee> test = employeeRepo.findById(id);
		if (test.isEmpty()) {
			return "id is not present";
		} else {
			employeeRepo.deleteById(id);
			return "Employee id " + id + " deleted successfully";
		}

	}

	@Override
	public Employee update(Employee employee) {

		long employeeId = employee.getEmployee_id();
		try {
			Employee emp = findById(employeeId);
			if (emp == null) {
				throw new EmployeeNotFoundException("employeeId not found for ID: " + emp);
			}

			return employeeRepo.save(employee);
		} catch (Exception e) {
			throw new ServiceException("Error updating employee: " + e.getMessage(), e);
		}

	}

	@Override
	public void assignTestToEmployee(Long employeeId, Long testId) {
		try {
			// Check if the employee exists
			Employee employee = employeeRepo.findById(employeeId).orElse(null);
			if (employee == null) {
				throw new EntityNotFoundException("Employee with ID " + employeeId + " not found.");
			}

			String testUrl = "http://localhost:8081/api/v1/test/" + testId; // Corrected URL

			QuestionTest testDto = restTemplate.getForObject(testUrl, QuestionTest.class);
			AssignTest assignTest = new AssignTest();
			assignTest.setEmployee_id(employee.getEmployee_id());
			assignTest.setTest_id(testDto.getTestId());

			assignTestRepo.save(assignTest);

			// Use the retrieved test details

		} catch (EntityNotFoundException e) {
			throw e; // Rethrow EntityNotFoundException directly
		} catch (Exception e) {
			throw new ServiceException("Error assignTestToEmployee: " + e.getMessage(), e);
		}
	}

	// }
//		Optional<Employee> optionalEmployee = employeeRepo.findById(employeeId);
//		 Optional<QuestionTest> optionalTest = questionTestRepo.findById(testId);
//		if(optionalEmployee.isPresent()) {
//			  Employee employee = optionalEmployee.get();
//			  if(optionalTest.isPresent()) {
//			 QuestionTest test = optionalTest.get();
//			 test.getTestId();
//		}    
//		else 

//		 
//		        RestTemplate restTemplate = new RestTemplate();
//		        String assignTestUrl = EMPLOYEE_SERVICE_URL + "/api/v1/employees/assign-test";
//		        ResponseEntity<String> response = restTemplate.postForEntity(assignTestUrl, null, String.class, employeeId, testId);
//
//		        if (response.getStatusCode().is2xxSuccessful()) {
//		            System.out.println("Test assigned successfully");
//		        } else {
//		            System.err.println("Failed to assign test: " + response.getBody());
//		        }
	// }

//	@Override
//	public List<QuestionTest> getAllAssignedTests(Long employeeId) {
//        Optional<Employee> optionalEmployee = employeeRepo.findById(employeeId);
//        if (optionalEmployee.isPresent()) {
//            Employee employee = optionalEmployee.get();
//            return employee.getTests(); 
//        } else {
//            throw new EntityNotFoundException("Employee with ID " + employeeId + " not found.");
//        }
//    }

	@Override
	public boolean login(String email, String password) {
		Optional<Employee> optionalEmployee = employeeRepo.findByEmail(email);
		if (optionalEmployee.isPresent()) {
			Employee employee = optionalEmployee.get();
			return employee.getPassword().equals(password);
		}
		return false;

	}

	@Override
	public AssignTestResponse getAllAssignedTests(Long employeeId) {
		Employee employee = employeeRepo.findById(employeeId).orElse(null);
		if (employee == null) {
			throw new RuntimeException("Employee with ID " + employeeId + " not found.");
		}
		List<AssignTest> test = assignTestRepo.findByEmployeeId(employeeId);
		AssignTestResponse response = new AssignTestResponse();
		response.setEmployee_id(employee.getEmployee_id());
		response.setFirstName(employee.getFirstName());
		response.setLastName(employee.getLastName());
		response.setEmail(employee.getEmail());
		response.setPassword(employee.getPassword());
		response.setList(test);

		return response;
	}

//	@Override
//	public List<AssignTestResponse> getAllAssignedTests(Long employeeId) {
//		 Optional<AssignTest> assignedTest = assignTestRepo.findById(employeeId);
//	        if (assignedTest.isPresent()) {
//	            throw new EntityNotFoundException("No assigned tests found for employee with ID " + employeeId);
//
//	        } else {
//	            throw new EntityNotFoundException("Employee with ID " + employeeId + " not found.");
//	        }
//	}

}
