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
public class EmployeeTrendDto {
  private LocalDate date;
  private long count;
  private String status; // ACTIVE, ON_LEAVE, RESIGNED 등
}