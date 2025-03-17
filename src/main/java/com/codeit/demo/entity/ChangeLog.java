package com.codeit.demo.entity;

import com.codeit.demo.entity.enums.ChangeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "change_log")
public class ChangeLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ChangeType type;

  @Column(name = "employee_number", nullable = false, length = 50)
  private String employeeNumber;

  @Column(length = 255)
  private String memo;

  @Column(name = "ip_address", length = 255)
  private String ipAddress;

  @Column(name = "at", nullable = false)
  private ZonedDateTime at;

  // Getters and Setters
}
