package com.codeit.demo.service;

import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DepartmentService {

  Page<DepartmentDto> getAllDepartments(String nameOrDescription, Pageable pageable);

  /**
   * ID로 부서 정보를 조회합니다.
   *
   * @param id 부서 ID
   * @return 부서 정보
   */
  DepartmentDto getDepartmentById(Long id);

  /**
   * 새로운 부서를 생성합니다.
   *
   * @param request 부서 생성 요청 정보
   * @return 생성된 부서 정보
   */
  DepartmentDto createDepartment(DepartmentCreateRequest request);

  /**
   * 부서 정보를 수정합니다.
   *
   * @param id 수정할 부서 ID
   * @param request 수정 요청 정보
   * @return 수정된 부서 정보
   */
  DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request);

  /**
   * 부서를 삭제합니다.
   *
   * @param id 삭제할 부서 ID
   */
  void deleteDepartment(Long id);

  /**
   * 부서에 소속된 직원 수를 업데이트합니다.
   *
   * @param departmentId 부서 ID
   * @return 업데이트된 부서 정보
   */
  DepartmentDto updateEmployeeCount(Long departmentId);
}