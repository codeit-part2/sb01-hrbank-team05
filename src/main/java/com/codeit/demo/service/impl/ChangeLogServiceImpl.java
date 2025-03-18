package com.codeit.demo.service.impl;

import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.entity.ChangeLog;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.ChangeType;
import com.codeit.demo.mapper.ChangeLogMapper;
import com.codeit.demo.repository.ChangeLogRepository;
import com.codeit.demo.service.ChangeDescriptionService;
import com.codeit.demo.service.ChangeLogService;
import com.codeit.demo.util.ClientInfo;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeDescriptionService changeDescriptionService;

  private final ChangeLogRepository changeLogRepository;

  private final ChangeLogMapper changeLogMapper;

  private final ClientInfo clientInfo;

  @Transactional
  @Override
  public void createChangeLogForCreation(Employee employee,
      EmployeeCreateRequest employeeCreateRequest) {
    // change_log 추가
    String ipAddress = clientInfo.getIpAddr();
    ChangeLog changeLog = changeLogRepository.save(
        new ChangeLog(ChangeType.CREATED,
            employee.getEmployeeNumber(),
            (employeeCreateRequest.memo() != null) ? employeeCreateRequest.memo()
                : "신규 직원 등록",
            ipAddress,
            Instant.now()));
    // change_description 추가
    changeDescriptionService.createChangeDescriptionForCreation(changeLog, employeeCreateRequest);
  }

  @Transactional
  @Override
  public void createChangeLogForUpdate(Employee employee,
      EmployeeUpdateRequest employeeUpdateRequest) {
    // change_log 추가
    String ipAddress = clientInfo.getIpAddr();

    ChangeLog changeLog = changeLogRepository.save(
        new ChangeLog(ChangeType.UPDATED,
            employee.getEmployeeNumber(),
            (employeeUpdateRequest.memo() != null) ? employeeUpdateRequest.memo()
                : "직원 정보 수정",
            ipAddress,
            Instant.now()));
    // change_description 추가
    changeDescriptionService.createChangeDescriptionForUpdate(changeLog, employee,
        employeeUpdateRequest);
  }

  @Transactional
  @Override
  public void createChangeLogForDeletion(Employee employee) {
    // change_log 추가
    String ipAddress = clientInfo.getIpAddr();
    ChangeLog changeLog = changeLogRepository.save(
        new ChangeLog(ChangeType.DELETED,
            employee.getEmployeeNumber(),
            "직원 삭제",
            ipAddress,
            Instant.now()));
    // change_description 추가
    changeDescriptionService.createChangeDescriptionForDeletion(changeLog, employee);
  }
}
