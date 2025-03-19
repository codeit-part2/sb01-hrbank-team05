package com.codeit.demo.repository;

import com.codeit.demo.entity.Department;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

  Page<Department> findByNameContainingOrDescriptionContaining(
      String name, String description, Pageable pageable);

  @Modifying
  @Query("UPDATE Department d SET d.employeeCount = COALESCE(d.employeeCount, 0) + 1 WHERE d.id = :departmentId")
  void incrementEmployeeCount(@Param("departmentId") Long departmentId);

  @Modifying
  @Query("UPDATE Department d SET d.employeeCount = GREATEST(COALESCE(d.employeeCount, 0) - 1, 0) WHERE d.id = :departmentId")
  void decrementEmployeeCount(@Param("departmentId") Long departmentId);

}