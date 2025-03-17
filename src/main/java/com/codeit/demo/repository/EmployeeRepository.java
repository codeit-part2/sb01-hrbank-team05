package com.codeit.demo.repository;

import com.codeit.demo.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("select e.hireDate, count(e.id)" +
            " from Employee e" +
            " where e.hireDate between :from and :to" +
            " group by e.hireDate" +
            " order by e.hireDate")
    List<Object[]> findTrend(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("select count(e.id) from Employee e" +
            " where e.hireDate<=:date")
    Integer findTotalCountByDate(@Param("date") LocalDate date);
}