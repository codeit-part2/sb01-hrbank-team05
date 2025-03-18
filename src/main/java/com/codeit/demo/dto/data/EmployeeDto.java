package com.codeit.demo.dto.data;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
  private Long id;
  private String name;
  private String email;
  private String employeeNumber;
  private Long departmentId;
  private String departmentName;
  private String position;
  private LocalDate hireDate;
  private String status;
  private Integer profileImageId;
}