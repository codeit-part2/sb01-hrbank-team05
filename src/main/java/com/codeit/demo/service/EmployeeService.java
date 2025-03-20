package com.codeit.demo.service;

import com.codeit.demo.dto.data.CursorPageResponseEmployeeDto;
import com.codeit.demo.dto.data.EmployeeDistributionDto;
import com.codeit.demo.dto.data.EmployeeDto;
import com.codeit.demo.dto.data.EmployeeTrendDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface EmployeeService {

  // 새로운 직원 생성
  EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile profileImage)
      throws IOException;

  // ID로 직원 조회
  EmployeeDto getEmployeeById(Long id);

  // 페이지 네이션 사용 하여 모든 직원 조회
  public CursorPageResponseEmployeeDto findAllEmployees(
      String nameOrEmail,
      String employeeNumber,
      String departmentName,
      String position,
      LocalDate hireDateFrom,
      LocalDate hireDateTo,
      String status,
      Long idAfter,
      Object cursor,
      int size,
      String sortField,
      String sortDirection);



  // 직원 정보 수정
  EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request);

  // 프로필 이미지 수정
  EmployeeDto updateProfileImage(Long id, MultipartFile profileImage) throws IOException;

  // 직원 상태 수정
  EmployeeDto updateEmployeeStatus(Long id, String status);

  // 직원 삭제
  void deleteEmployee(Long id);


  // 전체 직원 수 조회
  long countEmployees(String status, LocalDate startDate, LocalDate endDate);


  // 부서 ID로 소속 부서 직원 조회
  CursorPageResponseEmployeeDto getEmployeesByDepartment(Long departmentId, Long idAfter, int size);

  // 직원 추이 조회
  List<EmployeeTrendDto> findTrends(LocalDate from, LocalDate to, String unit);

  // 직원 분포 통계 조회
  List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy);

}
