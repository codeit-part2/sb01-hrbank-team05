package com.codeit.demo.service.impl;

import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.entity.ChangeDescription;
import com.codeit.demo.entity.ChangeLog;
import com.codeit.demo.entity.Department;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.EmploymentStatus;
import com.codeit.demo.entity.enums.PropertyName;
import com.codeit.demo.mapper.ChangeDescriptionMapper;
import com.codeit.demo.repository.ChangeDescriptionRepository;
import com.codeit.demo.repository.ChangeLogRepository;
import com.codeit.demo.repository.DepartmentRepository;
import com.codeit.demo.service.ChangeDescriptionService;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChangeDescriptionImpl implements ChangeDescriptionService {

  public class ChangeDetail {

    PropertyName propertyName;
    String before;
    String after;

    public ChangeDetail(PropertyName propertyName, String before, String after) {
      this.propertyName = propertyName;
      this.before = before;
      this.after = after;
    }
  }

  private final ChangeLogRepository changeLogRepository;
  private final ChangeDescriptionRepository changeDescriptionRepository;
  private final DepartmentRepository departmentRepository;

  private final ChangeDescriptionMapper changeDescriptionMapper;

  @Override
  public void createChangeDescriptionForCreation(ChangeLog changeLog,
      EmployeeCreateRequest employeeCreateRequest) {
    // 직원 생성 시에는 모든 정보가 이력에 추가됨
    // 부서명 조회
    String departmentName = departmentRepository.findById(employeeCreateRequest.departmentId())
        .map(Department::getName)
        .orElse(null);
    // 변경 이력 추가할 속성 목록
    List<ChangeDescription> changeDescriptions = List.of(
        new ChangeDescription(PropertyName.NAME, null, employeeCreateRequest.name(), changeLog),
        new ChangeDescription(PropertyName.EMAIL, null, employeeCreateRequest.email(),
            changeLog),
        new ChangeDescription(PropertyName.DEPARTMENT_NAME, null, departmentName, changeLog),
        new ChangeDescription(PropertyName.POSITION, null, employeeCreateRequest.position(),
            changeLog),
        new ChangeDescription(PropertyName.HIRE_DATE, null,
            String.valueOf(employeeCreateRequest.hireDate()), changeLog),
        new ChangeDescription(PropertyName.STATUS, null, String.valueOf(EmploymentStatus.ACTIVE),
            changeLog)
    );
    changeDescriptionRepository.saveAll(changeDescriptions);
  }

  @Override
  public void createChangeDescriptionForUpdate(ChangeLog changeLog, Employee employee,
      EmployeeUpdateRequest employeeUpdateRequest) {
    List<ChangeDetail> changes = compareEmployeeChange(employee, employeeUpdateRequest);
    List<ChangeDescription> changeDescriptions = changes.stream()
        .map(changeDetail -> new ChangeDescription(changeDetail.propertyName, changeDetail.before,
            changeDetail.after, changeLog))
        .toList();
    if (!changeDescriptions.isEmpty()) {
      changeDescriptionRepository.saveAll(changeDescriptions);
    }
  }

  @Override
  public void createChangeDescriptionForDeletion(ChangeLog changeLog, Employee employee) {
    // 직원 삭제 시에는 기존 직원의 정보가 모두 before로, after는 null로 들어감
    // 부서명 조회
    String departmentName = employee.getDepartment().getName();
    // 변경 이력 추가할 속성 목록
    List<ChangeDescription> changeDescriptions = List.of(
        new ChangeDescription(PropertyName.NAME, employee.getName(),
            null, changeLog),
        new ChangeDescription(PropertyName.EMAIL, employee.getEmail(),
            null,
            changeLog),
        new ChangeDescription(PropertyName.DEPARTMENT_NAME, departmentName, null,
            changeLog),
        new ChangeDescription(PropertyName.POSITION, employee.getPosition(),
            null,
            changeLog),
        new ChangeDescription(PropertyName.HIRE_DATE, String.valueOf(employee.getHireDate()),
            null, changeLog),
        new ChangeDescription(PropertyName.STATUS, String.valueOf(employee.getStatus()),
            null,
            changeLog)
    );
    changeDescriptionRepository.saveAll(changeDescriptions);
  }

  // 변경된 속성 정보만 구하기
  private List<ChangeDetail> compareEmployeeChange(Employee employee,
      EmployeeUpdateRequest employeeUpdateRequest) {
    List<ChangeDetail> changes = new ArrayList<>();
    if (isDifferent(employee.getName(), employeeUpdateRequest.name())) {
      changes.add(
          new ChangeDetail(PropertyName.NAME, employee.getName(), employeeUpdateRequest.name()));
    }
    if (isDifferent(employee.getEmail(), employeeUpdateRequest.email())) {
      changes.add(
          new ChangeDetail(PropertyName.EMAIL, employee.getEmail(),
              employeeUpdateRequest.email()));
    }
    if (isDifferent(employee.getDepartment().getId(), employeeUpdateRequest.departmentId())) {
      String beforeDepartmentName = employee.getDepartment().getName();
      String afterDepartmentName = departmentRepository.findById(
              employeeUpdateRequest.departmentId())
          .map(Department::getName)
          .orElse(null);
      changes.add(
          new ChangeDetail(PropertyName.DEPARTMENT_NAME, beforeDepartmentName,
              afterDepartmentName));
    }
    if (isDifferent(employee.getPosition(), employeeUpdateRequest.position())) {
      changes.add(
          new ChangeDetail(PropertyName.POSITION, employee.getPosition(),
              employeeUpdateRequest.position()));
    }
    if (isDifferent(employee.getHireDate(), employeeUpdateRequest.hireDate())) {
      changes.add(
          new ChangeDetail(PropertyName.HIRE_DATE, String.valueOf(employee.getHireDate()),
              String.valueOf(employeeUpdateRequest.hireDate())));
    }
    if (isDifferent(employee.getStatus(), employeeUpdateRequest.status())) {
      changes.add(
          new ChangeDetail(PropertyName.STATUS, String.valueOf(employee.getStatus()),
              String.valueOf(employeeUpdateRequest.status())));
    }
    return changes;
  }


  private boolean isDifferent(Object before, Object after) {
    if (before != null && after == null) {
      return false; // PATCH이므로 UpdateRequset의 null인 필드는 정보 수정을 안한 필드로 간주
    }
    if (before == null && after == null) {
      return false; // NullPointerException 방지
    }
    if (!before.equals(after)) {
      return true;
    }
    return false;
  }
}
