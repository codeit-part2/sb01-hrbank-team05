package com.codeit.demo.service;

import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

  // 모든 부서 조회
  Page<DepartmentDto> getAllDepartments(String nameOrDescription, Pageable pageable);

  // 아이디로 부서 조회
  DepartmentDto getDepartmentById(Long id);

  // 부서 생성
  DepartmentDto createDepartment(DepartmentCreateRequest request);

  // 부서 업데이트
  DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request);

  // 부서 삭제
  void deleteDepartment(Long id);

  // 부서 소속 직원수 업데이트
  DepartmentDto updateEmployeeCount(Long departmentId);
}