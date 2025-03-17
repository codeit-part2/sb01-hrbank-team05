package com.codeit.demo.service;

import com.codeit.demo.dto.data.EmployeeDistributionDto;
import com.codeit.demo.dto.data.EmployeeDto;
import com.codeit.demo.dto.data.EmployeeTrendDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.dto.response.CursorPageResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {
  /**
   * 새로운 직원을 생성합니다.
   *
   * @param request 직원 생성 요청 데이터
   * @param profileImage 직원 프로필 이미지 (선택적)
   * @return 생성된 직원 정보
   */
  EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage)
      throws IOException;

  /**
   * 직원 ID로 직원 정보를 조회합니다.
   *
   * @param id 직원 ID
   * @return 직원 정보 DTO
   */
  EmployeeDto getEmployeeById(Long id);

  /**
   * 모든 직원 목록을 페이지네이션하여 조회합니다.
   *
   * @param cursor 커서 ID (첫 페이지는 null)
   * @param size 페이지 크기
   * @return 커서 기반 페이지 응답
   */
  CursorPageResponse<EmployeeDto> getAllEmployees(
      String nameOrEmail,
      String employeeNumber,
      String departmentName,
      String position,
      LocalDate hireDateFrom,
      LocalDate hireDateTo,
      String status,
      Long idAfter,
      String cursor,
      int size,
      String sortField,
      String sortDirection);



  /**
   * 직원 정보를 수정합니다.
   *
   * @param id 직원 ID
   * @param request 직원 정보 수정 요청
   * @return 수정된 직원 정보
   */
  EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request);

  /**
   * 프로필 이미지를 업데이트합니다.
   *
   * @param id 직원 ID
   * @param profileImage 새 프로필 이미지
   * @return 업데이트된 직원 정보
   */
  EmployeeDto updateProfileImage(Long id, MultipartFile profileImage) throws IOException;

  /**
   * 직원 상태를 업데이트합니다.
   *
   * @param id 직원 ID
   * @param status 새로운 고용 상태
   * @return 업데이트된 직원 정보
   */
  EmployeeDto updateEmployeeStatus(Long id, String status);

  /**
   * 직원을 삭제합니다 (상태를 비활성으로 변경).
   *
   * @param id 직원 ID
   */
  void deleteEmployee(Long id);

  /**
   * 전체 직원 수를 조회합니다.
   *
   * @return 전체 직원 수
   */
  long countEmployees(String status, LocalDate startDate, LocalDate endDate);


  /**
   * 부서 ID로 직원 목록을 조회합니다.
   *
   * @param departmentId 부서 ID
   * @param cursor 커서 ID
   * @param size 페이지 크기
   * @return 커서 기반 페이지 응답
   */
  CursorPageResponse<EmployeeDto> getEmployeesByDepartment(Long departmentId, Long cursor, int size);

  List<EmployeeTrendDto> getEmployeeTrends(LocalDate startDate, LocalDate endDate, String status);

  /**
   * 직원 분포 통계를 조회합니다.
   *
   * @return 직원 분포 통계 정보
   */
  /**
   * 직원 분포 통계를 조회합니다.
   *
   * @param groupBy 그룹화 기준 (department, position, status)
   * @return 직원 분포 통계 정보 목록
   */
  List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy);

}