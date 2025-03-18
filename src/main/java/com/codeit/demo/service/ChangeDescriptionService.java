package com.codeit.demo.service;

import com.codeit.demo.dto.data.DiffDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.entity.ChangeLog;
import com.codeit.demo.entity.Employee;
import java.util.List;

public interface ChangeDescriptionService {

  // 직원 생성 시
  void createChangeDescriptionForCreation(ChangeLog changeLog,
      EmployeeCreateRequest employeeCreateRequest);

  // 직원 수정 시
  void createChangeDescriptionForUpdate(ChangeLog changeLog, Employee employee,
      EmployeeUpdateRequest employeeUpdateRequest);

  // 직원 삭제 시
  void createChangeDescriptionForDeletion(ChangeLog changeLog, Employee employee);

  // 상세 이력 조회
  List<DiffDto> findAllChangeDescriptionsByLogId(Long logId);
  
}
