package com.example.demo.entity;

public class AssignTestDto {
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
	@Override
	public String toString() {
		return "AssignTestDto [employee_id=" + employee_id + ", test_id=" + test_id + "]";
	}
	
    
}
