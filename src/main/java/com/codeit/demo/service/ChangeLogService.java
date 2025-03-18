package com.codeit.demo.service;

import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.entity.Employee;
import java.time.Instant;

public interface ChangeLogService {

  // 직원 생성 시 이력 생성
  void createChangeLogForCreation(Employee employee, EmployeeCreateRequest employeeCreateRequest);

  // 직원 정보 수정 시 이력 생성
  void createChangeLogForUpdate(Employee employee, EmployeeUpdateRequest employeeUpdateRequest);

  // 직원 삭제 시 이력 생성
  void createChangeLogForDeletion(Employee employee);

  // 수정 이력 건수 조회
  long countLogs(Instant fromDate, Instant toDate);
}
