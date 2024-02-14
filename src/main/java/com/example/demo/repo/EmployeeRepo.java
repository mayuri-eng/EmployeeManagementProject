package com.example.demo.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Employee;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long>{

	Optional<Employee> findByEmail(String email);


	//Employee findByEmail(String email);

	//public void save();

	
}
