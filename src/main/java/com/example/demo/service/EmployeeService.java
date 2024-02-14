package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.entity.QuestionTest;


public interface EmployeeService {

	//Employee save(Employee employee);

	List<Employee> findAll();

	Employee findById(Long id);

	Employee update(Employee employee);

	String delete(Long id);

//	void assignTest(Long employeeId, Employee test);

	void assignTestToEmployee(Long employeeId, Long testId);

	List<QuestionTest> getAllAssignedTests(Long employeeId);

	Employee register(Employee employee);

	boolean login(String email, String password);

}
