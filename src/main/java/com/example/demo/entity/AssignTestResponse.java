package com.example.demo.entity;

import java.util.List;

public class AssignTestResponse {

	private Long employee_id;
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private List<AssignTest> list;
	public Long getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(Long employee_id) {
		this.employee_id = employee_id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<AssignTest> getList() {
		return list;
	}
	public void setList(List<AssignTest> list) {
		this.list = list;
	}
	@Override
	public String toString() {
		return "AssignTestResponse [employee_id=" + employee_id + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", password=" + password + ", list=" + list + "]";
	}
	
	
}
