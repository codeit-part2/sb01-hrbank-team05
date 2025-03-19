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
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "change_log")
@NoArgsConstructor
@Getter
public class ChangeLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "type", nullable = false)
  private ChangeType type;

  @Column(name = "employee_number", nullable = false, length = 50)
  private String employeeNumber;

  @Column(nullable = true, length = 255)
  private String memo;

  @Column(name = "ip_address", length = 255)
  private String ipAddress;

  @Column(nullable = false)
  private Instant at;

  public ChangeLog(ChangeType type, String employeeNumber, String memo, String ipAddress,
      Instant at) {
    this.type = type;
    this.employeeNumber = employeeNumber;
    this.memo = memo;
    this.ipAddress = ipAddress;
    this.at = at;
  }

}
