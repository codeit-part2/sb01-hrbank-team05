package com.codeit.demo.mapper;

import com.codeit.demo.dto.data.EmployeeDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.entity.BinaryContent;
import com.codeit.demo.entity.Department;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.EmploymentStatus;
import java.time.LocalDate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
    imports = {LocalDate.class, EmploymentStatus.class})
public interface EmployeeMapper {

  // Employee 엔터티를 EmployeeDto로 변환
  @Mapping(source = "department.name", target = "departmentName")
  @Mapping(source = "department.id", target = "departmentId")
  @Mapping(source = "status", target = "status")
  EmployeeDto employeeToEmployeeDto(Employee employee);

  // EmployeeCreateRequest를 Employee로 변환 (신규 직원 생성 시)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "department", source = "departmentId", qualifiedByName = "departmentIdToDepartment")
  @Mapping(target = "hireDate", source = "hireDate")
  @Mapping(target = "status", expression = "java(EmploymentStatus.ACTIVE)")
  @Mapping(target = "profileImage", ignore = true)
  Employee employeeCreateRequestToEmployee(EmployeeCreateRequest request);

  // EmployeeUpdateRequest를 Employee로 변환 (직원 정보 업데이트 시)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "department", source = "departmentId", qualifiedByName = "departmentIdToDepartment")
  @Mapping(target = "hireDate", source = "hireDate")
  @Mapping(target = "status", source = "status")
  @Mapping(target = "profileImage", ignore = true)
  void updateEmployeeFromRequest(EmployeeUpdateRequest request, @MappingTarget Employee employee);

  // 부서 ID를 Department 객체로 변환하는 메서드
  @Named("departmentIdToDepartment")
  default Department departmentIdToDepartment(Long departmentId) {
    if (departmentId == null) {
      return null;
    }
    Department department = new Department();
    department.setId(departmentId);
    return department;
  }

  // 프로필 이미지 설정을 위한 기본 메서드
  default Employee setProfileImage(Employee employee, BinaryContent profileImage) {
    employee.setProfileImage(profileImage);
    return employee;
  }

}