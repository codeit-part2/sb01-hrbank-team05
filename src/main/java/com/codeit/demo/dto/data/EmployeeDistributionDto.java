package com.codeit.demo.dto.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDistributionDto {
  private String groupKey;  // 그룹화 기준 (부서명/직급/상태)
  private long count;       // 직원 수
  private double percentage; // 백분율
}