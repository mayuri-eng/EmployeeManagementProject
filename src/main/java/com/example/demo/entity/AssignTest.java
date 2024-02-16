package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "assignTest")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssignTest {
	@Override
	public String toString() {
		return "AssignTest [id=" + id + ", employee_id=" + employee_id + ", test_id=" + test_id + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long employee_id;
	private Long test_id;

	public Long getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}

	public Long getTest_id() {
		return test_id;
	}

	public void setTest_id(Long test_id) {
		this.test_id = test_id;
	}

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AssignTest(Long id, Long employee_id, Long test_id) {
		super();
		this.id = id;
		this.employee_id = employee_id;
		this.test_id = test_id;
	}

	public AssignTest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
