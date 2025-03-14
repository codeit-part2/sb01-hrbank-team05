package com.codeit.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;

@Entity
@Table(name = "department", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "description", nullable = false, length = 255)
  private String description;

  @Column(name = "established_date", nullable = false)
  private LocalDate establishedDate;

  @Column(name = "employee_count", nullable = false)
  private int employeeCount = 0;

  public Department() {
  }

  public Department(String name, String description, LocalDate establishedDate) {
    this.name = name;
    this.description = description;
    this.establishedDate = establishedDate;
    this.employeeCount = 0;
  }

  // ID 조회
  public Long findId() {
    return id;
  }

  // 부서 이름 조회;
  public String findName() {
    return name;
  }

  // 부서 이름 설정;
  public void setName(String name) {
    this.name = name;
  }

  // 부서 설명 조회;
  public String findDescription() {
    return description;
  }

  // 부서 설명 설정;
  public void setDescription(String description) {
    this.description = description;
  }

  // 부서 설립일 조회;
  public LocalDate findEstablishedDate() {
    return establishedDate;
  }

  // 부서 설립일 설정;
  public void setEstablishedDate(LocalDate establishedDate) {
    this.establishedDate = establishedDate;
  }

  // 소속 직원 수 조회;
  public int findEmployeeCount() {
    return employeeCount;
  }

  // 소속 직원 수 설정;
  public void setEmployeeCount(int employeeCount) {
    this.employeeCount = employeeCount;
  }
}
