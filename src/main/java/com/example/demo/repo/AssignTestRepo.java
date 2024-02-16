package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.AssignTest;
@Repository
public interface AssignTestRepo extends JpaRepository<AssignTest, Long>{

	@Query("SELECT a FROM AssignTest a WHERE a.employee_id = :employeeId")
    List<AssignTest> findByEmployeeId(@Param("employeeId") Long employeeId);
}
