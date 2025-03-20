package com.codeit.demo.repository;

import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.EmploymentStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

  // 이메일로 직원 찾기
  @EntityGraph(attributePaths = {"department", "profileImage"})
  Optional<Employee> findByEmail(String email);

  // 부서별 조회
  @EntityGraph(attributePaths = "department")
  Page<Employee> findByDepartmentId(Long departmentId, Pageable pageable);

  Long countByDepartmentId(Long departmentId);

  @EntityGraph(attributePaths = {"department", "profileImage"})
  Optional<Employee> findTopByEmployeeNumberLikeOrderByEmployeeNumberDesc(String pattern);

  @Query("SELECT e FROM Employee e " +
      "WHERE (:nameOrEmail IS NULL OR e.name LIKE %:nameOrEmail% OR e.email LIKE %:nameOrEmail%) AND " +
      "(:employeeNumber IS NULL OR e.employeeNumber LIKE %:employeeNumber%) AND " +
      "(:departmentName IS NULL OR e.department.name LIKE %:departmentName%) AND " +
      "(:position IS NULL OR e.position LIKE %:position%) AND " +
      "(:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom) AND " +
      "(:hireDateTo IS NULL OR e.hireDate <= :hireDateTo) AND " +
      "(:status IS NULL OR CAST(e.status AS string) = :status)")
  @EntityGraph(attributePaths = {"department", "profileImage"})
  Page<Employee> findEmployeesWithAdvancedFilters(
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("status") String status,
      Pageable pageable);

  @Query("SELECT COUNT(e) FROM Employee e " +
      "WHERE (:status IS NULL OR CAST(e.status AS string) = CAST(:status AS string)) " +
      "AND (:startDate IS NULL OR e.hireDate >= :startDate) " +
      "AND (:endDate IS NULL OR e.hireDate <= :endDate)")
  long countEmployeesByFilters(@Param("status") EmploymentStatus status,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);

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


  //lastBackupTime 이후에 변경된 직원 데이터가 있는지 확인
  boolean existsByUpdatedAtAfter(LocalDateTime lastBackupTime);

  @Query("SELECT COUNT(e) FROM Employee e " +
      "WHERE (:status IS NULL OR CAST(e.status AS string)  = :status) " +
      "AND (:startDate IS NULL OR e.hireDate >= :startDate) " +
      "AND (:endDate IS NULL OR e.hireDate <= :endDate)")
  long countEmployeesByFilters(@Param("status") String status,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}