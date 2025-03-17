package com.codeit.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "department", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(length = 255)
  private String description;

  @Column(name = "established_date")
  private LocalDate establishedDate;

  @Column(name = "employee_count")
  private Integer employeeCount;

  public Department() {
  }

  public Department(String name, String description, LocalDate establishedDate,
      Integer employeeCount) {
    this.name = name;
    this.description = description;
    this.establishedDate = establishedDate;
    this.employeeCount = employeeCount;
  }



}
