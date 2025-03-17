package com.codeit.demo.service;

import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.dto.response.CursorPageResponse;

public interface DepartmentService {

  /**
   * 모든 부서 목록을 페이징해서 조회합니다.
   *
   * @param cursor 커서 (다음 페이지 시작점)
   * @param size 페이지 크기
   * @param nameOrDescription 부서 이름 또는 설명으로 검색
   * @param sortField 정렬 필드 (name 또는 establishedDate)
   * @param sortDirection 정렬 방향 (asc 또는 desc)
   * @return 페이징된 부서 목록
   */
  CursorPageResponse<DepartmentDto> getAllDepartments(Long cursor, int size, String nameOrDescription,
      String sortField, String sortDirection);

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