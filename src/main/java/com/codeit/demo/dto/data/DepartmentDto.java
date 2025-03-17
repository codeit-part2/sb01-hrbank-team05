package com.codeit.demo.dto.data;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentDto {
  private Long id;
  private String name;
  private String description;
  private LocalDateTime establishedDate;
  private Integer employeeCount;
}