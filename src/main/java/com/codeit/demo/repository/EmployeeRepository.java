package com.codeit.demo.repository;

import com.codeit.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByName(String name);
    @Query("SELECT COUNT(e.id) FROM Employee e " +
            "WHERE e.hireDate <= :date AND e.status != 'RESIGNED'")
    Integer findTotalCountNoResigned(@Param("date") LocalDate date);
}