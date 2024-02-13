package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;


public interface EmployeeService {

	Employee save(Employee employee);

	List<Employee> findAll();

	Employee findById(Long id);

	Employee update(Employee employee);

	String delete(Long id);

	void assignTest(Long employeeId, Employee test);

}
