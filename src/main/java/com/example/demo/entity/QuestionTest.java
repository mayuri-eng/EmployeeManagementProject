package com.example.demo.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class QuestionTest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long testId;
	private String testName;
	private String testDescription;

	public Long getTestId() {
		return testId;
	}

	public void setTestId(Long testId) {
		this.testId = testId;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}

	@Override
	public String toString() {
		return "QuestionTest [testId=" + testId + ", testName=" + testName + ", testDescription=" + testDescription
				+ "]";
	}

	public QuestionTest(Long testId, String testName, String testDescription) {
		super();
		this.testId = testId;
		this.testName = testName;
		this.testDescription = testDescription;
	}

	public QuestionTest() {
		super();
		// TODO Auto-generated constructor stub
	}

}
