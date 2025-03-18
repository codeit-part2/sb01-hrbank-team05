package com.codeit.demo.repository;

import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.EmploymentStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

  // 이메일로 직원 찾기
  Optional<Employee> findByEmail(String email);

  // 부서별 조회
  Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

  Long countByDepartmentId(Long departmentId);

  Optional<Employee> findTopByEmployeeNumberLikeOrderByEmployeeNumberDesc(String pattern);

  @Query("SELECT e FROM Employee e " +
      "LEFT JOIN e.department d " +
      "WHERE (:nameOrEmail IS NULL OR " +
      "      e.name LIKE CONCAT('%', CAST(:nameOrEmail AS string), '%') OR " +
      "      e.email LIKE CONCAT('%', CAST(:nameOrEmail AS string), '%')) AND " +
      "(:employeeNumber IS NULL OR e.employeeNumber LIKE CONCAT('%', CAST(:employeeNumber AS string), '%')) AND "
      +
      "(:departmentName IS NULL OR d.name LIKE CONCAT('%', CAST(:departmentName AS string), '%')) AND "
      +
      "(:position IS NULL OR e.position LIKE CONCAT('%', CAST(:position AS string), '%')) AND " +
      "(:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom) AND " +
      "(:hireDateTo IS NULL OR e.hireDate <= :hireDateTo) AND " +
      "(:status IS NULL OR e.status = :status)")
  Page<Employee> findEmployeesWithAdvancedFilters(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("status") EmploymentStatus status,
      Pageable pageable);

  Optional<Employee> findByName(String name);

  @Query("SELECT COUNT(e.id) FROM Employee e " +
      "WHERE e.hireDate <= :date AND e.status != 'RESIGNED'")
  Integer findTotalCountNoResigned(@Param("date") LocalDate date);

  @Query("SELECT d.name, COUNT(e) FROM Employee e JOIN e.department d GROUP BY d.name ORDER BY COUNT(e) DESC")
  List<Object[]> countEmployeesByDepartment();

  @Query("SELECT e.position, COUNT(e) FROM Employee e GROUP BY e.position ORDER BY COUNT(e) DESC")
  List<Object[]> countEmployeesByPosition();

  @Query("SELECT e.status, COUNT(e) FROM Employee e GROUP BY e.status ORDER BY COUNT(e) DESC")
  List<Object[]> countEmployeesByStatus();
}