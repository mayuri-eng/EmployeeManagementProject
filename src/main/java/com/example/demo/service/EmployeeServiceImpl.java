package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.QuestionTest;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.exception.QuestionTestNotFoundException;
import com.example.demo.repo.EmployeeRepo;
import com.example.demo.repo.QuestionTestRepo;

import jakarta.persistence.EntityNotFoundException;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	EmployeeRepo employeeRepo;
	
	@Autowired
	QuestionTestRepo questionTestRepo;

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
		Optional<Employee> optionalEmployee = employeeRepo.findById(employeeId);
	    Optional<QuestionTest> optionalTest = questionTestRepo.findById(testId);
	    
	    if (optionalEmployee.isPresent() && optionalTest.isPresent()) {
	        Employee employee = optionalEmployee.get();
	        QuestionTest test = optionalTest.get();
	        employee.getTests().add(test);
	        employeeRepo.save(employee);
	    } else {
	        throw new EntityNotFoundException("Employee or Test not found.");
	    }
	}
		catch (Exception e) {
		throw new ServiceException("Error assignTestToEmployee : " + e.getMessage(), e);
	}
	}
	@Override
	public List<QuestionTest> getAllAssignedTests(Long employeeId) {
        Optional<Employee> optionalEmployee = employeeRepo.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            return employee.getTests(); // Assuming you have a getter method for the tests in Employee entity
        } else {
            throw new EntityNotFoundException("Employee with ID " + employeeId + " not found.");
        }
    }

	@Override
	public boolean login(String email, String password) {
		   Optional<Employee> optionalEmployee = employeeRepo.findByEmail(email);
	        if (optionalEmployee.isPresent()) {
	            Employee employee = optionalEmployee.get();
	            return employee.getPassword().equals(password);
	        }
	        return false;

	}
	
}
