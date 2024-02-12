package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.QuestionTest;
import com.example.demo.exception.CategoryNotFoundException;
import com.example.demo.exception.QuestionTestNotFoundException;
import com.example.demo.repo.EmployeeRepo;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	
	@Autowired
	EmployeeRepo employeeRepo;
	
	

	@Override
	public Employee save(Employee employee) {
		try {
			return employeeRepo.save(employee);
		} catch (Exception e) {
			throw new QuestionTestNotFoundException("Error saving employee: " + e.getMessage());
		}
	}

	@Override
	public List<Employee> findAll() {
		try {
			return employeeRepo.findAll();
		} catch (Exception e) {
			throw new QuestionTestNotFoundException("Error saving Employee: " + e.getMessage());
		}
	}

	@Override
	public Employee findById(Long id) {
		try {
			Optional<Employee> employee = employeeRepo.findById(id);
			if (employee.isPresent()) {
				return employeeRepo.findById(id).get();
			} else {
				throw new Exception("Employee not found for ID: " + id);

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
				throw new Exception("employeeId not found for ID: " + emp);
			}

			return employeeRepo.save(employee);
		} catch (Exception e) {
			throw new ServiceException("Error updating employee: " + e.getMessage(), e);
		}

	}
}
