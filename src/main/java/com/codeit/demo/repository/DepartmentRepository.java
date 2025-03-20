package com.codeit.demo.repository;

import com.codeit.demo.entity.Department;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

  @Query("SELECT d FROM Department d " +
      "WHERE (:idAfter IS NULL OR d.id > :idAfter) AND " +
      "(:nameOrDescription IS NULL OR d.name LIKE %:nameOrDescription% OR d.description LIKE %:nameOrDescription%) " +
      "ORDER BY d.id ASC")
  List<Department> findDepartments(
      @Param("idAfter") Long idAfter,
      @Param("nameOrDescription") String nameOrDescription,
      Pageable pageable);


  @Modifying
  @Query("UPDATE Department d SET d.employeeCount = COALESCE(d.employeeCount, 0) + 1 WHERE d.id = :departmentId")
  void incrementEmployeeCount(@Param("departmentId") Long departmentId);

  @Modifying
  @Query("UPDATE Department d SET d.employeeCount = GREATEST(COALESCE(d.employeeCount, 0) - 1, 0) WHERE d.id = :departmentId")
  void decrementEmployeeCount(@Param("departmentId") Long departmentId);

}