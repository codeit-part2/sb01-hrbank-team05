package com.codeit.demo.repository;

import com.codeit.demo.entity.Employee;
import java.time.LocalDate;
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
      "WHERE (:idAfter IS NULL OR e.id > :idAfter) AND " +
      "(:cursor IS NULL OR e.id > :cursor) AND " +
      "(:nameOrEmail IS NULL OR e.name LIKE :nameOrEmail OR e.email LIKE :nameOrEmail) AND " +
      "(:employeeNumber IS NULL OR e.employeeNumber LIKE :employeeNumber) AND " +
      "(:departmentName IS NULL OR e.department.name LIKE :departmentName) AND " +
      "(:position IS NULL OR e.position LIKE :position) AND " +
      "(:hireDateFrom IS NULL OR e.hireDate >= :hireDateFrom) AND " +
      "(:hireDateTo IS NULL OR e.hireDate <= :hireDateTo) AND " +
      "(:status IS NULL OR CAST(e.status AS string) = :status) " +
      "ORDER BY e.id ASC")
  @EntityGraph(attributePaths = {"department", "profileImage"})
  List<Employee> findEmployeesWithAdvancedFilters(
      @Param("idAfter") Long idAfter,
      @Param("cursor") Long cursor,
      @Param("nameOrEmail") String nameOrEmail,
      @Param("employeeNumber") String employeeNumber,
      @Param("departmentName") String departmentName,
      @Param("position") String position,
      @Param("hireDateFrom") LocalDate hireDateFrom,
      @Param("hireDateTo") LocalDate hireDateTo,
      @Param("status") String status,
      Pageable pageable);

  @Query("SELECT e FROM Employee e WHERE e.department.id = :departmentId AND (:idAfter IS NULL OR e.id > :idAfter) ORDER BY e.id ASC")
  @EntityGraph(attributePaths = {"department", "profileImage"})
  List<Employee> findEmployeesByDepartment(
      @Param("departmentId") Long departmentId,
      @Param("idAfter") Long idAfter,
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

  @Query("SELECT COUNT(e) FROM Employee e " +
      "WHERE (:status IS NULL OR CAST(e.status AS string)  = :status) " +
      "AND (:startDate IS NULL OR e.hireDate >= :startDate) " +
      "AND (:endDate IS NULL OR e.hireDate <= :endDate)")
  long countEmployeesByFilters(@Param("status") String status,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}