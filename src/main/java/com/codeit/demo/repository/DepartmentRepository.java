package com.codeit.demo.repository;

import com.codeit.demo.entity.Department;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

  Page<Department> findByNameContainingOrDescriptionContaining(
      String name, String description, Pageable pageable);

  Page<Department> findByNameContainingOrDescriptionContainingAndIdGreaterThan(
      String name, String description, Long id, Pageable pageable);

  Page<Department> findByNameContainingOrDescriptionContainingAndIdLessThan(
      String name, String description, Long id, Pageable pageable);

  Page<Department> findByIdGreaterThan(Long id, Pageable pageable);

  Page<Department> findByIdLessThan(Long id, Pageable pageable);

}