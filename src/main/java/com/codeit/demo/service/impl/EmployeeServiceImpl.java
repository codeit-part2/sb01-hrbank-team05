package com.codeit.demo.service.impl;

import com.codeit.demo.dto.data.CursorPageResponseEmployeeDto;
import com.codeit.demo.dto.data.EmployeeDistributionDto;
import com.codeit.demo.dto.data.EmployeeDto;
import com.codeit.demo.dto.data.EmployeeTrendDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.entity.BinaryContent;
import com.codeit.demo.entity.Department;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.repository.TrendRepository;
import com.codeit.demo.service.ChangeLogService;
import com.codeit.demo.entity.enums.EmploymentStatus;
import com.codeit.demo.exception.DepartmentNotFoundException;
import com.codeit.demo.exception.DuplicateEmailException;
import com.codeit.demo.exception.EmployeeNotFoundException;
import com.codeit.demo.mapper.EmployeeMapper;
import com.codeit.demo.repository.DepartmentRepository;
import com.codeit.demo.repository.EmployeeRepository;
import com.codeit.demo.service.BinaryContentService;
import com.codeit.demo.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final EmployeeMapper employeeMapper;
  private final BinaryContentService binaryContentService;
  private final ChangeLogService changeLogService;
  private final TrendRepository trendRepository;


  @Override
  @Transactional
  public EmployeeDto createEmployee(EmployeeCreateRequest request, MultipartFile file)
      throws IOException {

    // 입력 유효성 검사
    if (request == null) {
      throw new IllegalArgumentException("직원 생성 요청 데이터가 필요합니다.");
    }

    // Employee 엔티티 생성
    Employee employee = employeeMapper.employeeCreateRequestToEmployee(request);

    // 사원번호 자동 생성
    employee.setEmployeeNumber(generateEmployeeNumber());

    // 이메일 중복 검사 @Transactional(readOnly = true)
    if (employeeRepository.findByEmail(request.email()).isPresent()) {
      throw new DuplicateEmailException("이메일이 이미 사용 중입니다: " + request.email());
    }

    // 부서 존재 여부 확인 및 연결
    var department = departmentRepository.findById(request.departmentId())
        .orElseThrow(() -> new DepartmentNotFoundException("부서를 찾을 수 없습니다: " + request.departmentId()));
    employee.setDepartment(department); // 부서 연결

    // 프로필 이미지가 제공된 경우에만 처리
    if (file != null && !file.isEmpty()) {

      BinaryContent savedFile = binaryContentService.createBinaryContent(file);
      employee.setProfileImage(savedFile);
    }
    // 생성된 직원의 부서가 있으면 직원 수 증가
    if (employee.getDepartment() != null) {
      departmentRepository.incrementEmployeeCount(employee.getDepartment().getId());
    }

    // 저장 및 DTO 변환
    Employee savedEmployee = employeeRepository.save(employee);

    // 직원 생성 시 이력 생성 코드 추가
    changeLogService.createChangeLogForCreation(savedEmployee, request);

    return employeeMapper.employeeToEmployeeDto(savedEmployee);
  }

  @Override
  @Transactional(readOnly = true)
  public EmployeeDto getEmployeeById(Long id) {
    return employeeRepository.findById(id)
        .map(employeeMapper::employeeToEmployeeDto)
        .orElseThrow(() -> new EmployeeNotFoundException("직원을 찾을 수 없습니다: " + id));
  }

  @Override
  @Transactional(readOnly = true)
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
      String sortDirection) {

    // 정렬 방향 설정
    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;

    // 정렬 필드 설정
    String sortProperty;
    switch (sortField.toLowerCase()) {
      case "employeenumber":
        sortProperty = "employeeNumber";
        break;
      case "hiredate":
        sortProperty = "hireDate";
        break;
      case "position":
        sortProperty = "position"; // position 정렬 옵션 추가
        break;
      case "name":
      default:
        sortProperty = "name";
        break;
    }

    // 페이지 및 정렬 정보 설정
    Sort sort = Sort.by(direction, sortProperty);

    Pageable pageable = PageRequest.of(0, size + 1, sort);

    List<Employee> employees = employeeRepository.findEmployeesWithAdvancedFilters(
        nameOrEmail,
        employeeNumber,
        departmentName,
        position,
        hireDateFrom,
        hireDateTo,
        status,
        idAfter,
        (Long) cursor,
        pageable);

    // 결과 처리 및 반환
    boolean hasNext = false;
    if (employees.size() > size) {
      hasNext = true;
      employees = employees.subList(0, size);
    }

    Long nextIdAfter = hasNext ? employees.get(employees.size() - 1).getId() : null;

    Long nextCursor = hasNext ? employees.get(employees.size() - 1).getId() : null;

    List<EmployeeDto> employeeDtos = employees.stream()
        .map(employeeMapper::employeeToEmployeeDto)
        .collect(Collectors.toList());

    return new CursorPageResponseEmployeeDto(
        employeeDtos,
        nextCursor != null ? nextCursor.toString() : null,
        nextIdAfter,
        size,
        employeeRepository.count(),
        hasNext
    );
  }

  @Override
  @Transactional
  public EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request) {
    log.info("Updating employee with ID: {}", id);

    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException("직원을 찾을 수 없습니다: " + id));

    // 이메일 중복 확인 (다른 직원이 같은 이메일을 사용하고 있는지)
    Optional<Employee> existingEmployee = employeeRepository.findByEmail(request.email());
    if (existingEmployee.isPresent() && !existingEmployee.get().getId().equals(id)) {
      throw new DuplicateEmailException("이메일이 이미 사용 중입니다: " + request.email());
    }

    // 부서 정보 업데이트 로직
    Department oldDepartment = employee.getDepartment();
    Long oldDepartmentId = oldDepartment != null ? oldDepartment.getId() : null;
    Long newDepartmentId = Long.valueOf(request.departmentId());

    // 부서 변경이 있을 경우에만 처리
    if (newDepartmentId != null && !Objects.equals(oldDepartmentId, newDepartmentId)) {
      // 새 부서 설정
      Department department = departmentRepository.findById(newDepartmentId)
          .orElseThrow(() -> new EntityNotFoundException("부서를 찾을 수 없습니다: " + newDepartmentId));
      employee.setDepartment(department);

      // 부서 직원 수 업데이트 (원자적 연산으로 처리)
      if (oldDepartmentId != null) {
        departmentRepository.decrementEmployeeCount(oldDepartmentId);
      }
      departmentRepository.incrementEmployeeCount(newDepartmentId);
    } else if (newDepartmentId == null && oldDepartmentId != null) {
      // 부서 제거 (부서 없음으로 변경)
      employee.setDepartment(null);
      departmentRepository.decrementEmployeeCount(oldDepartmentId);
    }
    // 직원 정보 수정 시 이력 생성 코드 추가
    changeLogService.createChangeLogForUpdate(employee, request);

    employeeMapper.updateEmployeeFromRequest(request, employee);

    Employee updatedEmployee = employeeRepository.save(employee);
    log.info("Employee updated with ID: {}", updatedEmployee.getId());

    return employeeMapper.employeeToEmployeeDto(updatedEmployee);
  }



  @Override
  @Transactional
  public EmployeeDto updateProfileImage(Long id, MultipartFile file) throws IOException {
    log.info("Updating profile image for employee with ID: {}", id);

    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException("직원을 찾을 수 없습니다: " + id));

    // 기존 이미지 삭제 처리
    if (employee.getProfileImage() != null) {
      binaryContentService.delete(employee.getProfileImage().getId());
    }

    // 새 이미지 저장
    BinaryContent savedFile = binaryContentService.createBinaryContent(file);
    employee.setProfileImage(savedFile);

    Employee updatedEmployee = employeeRepository.save(employee);
    return employeeMapper.employeeToEmployeeDto(updatedEmployee);
  }

  @Override
  @Transactional
  public EmployeeDto updateEmployeeStatus(Long id, String status) {
    log.info("Updating status to {} for employee with ID: {}", status, id);

    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException("직원을 찾을 수 없습니다: " + id));

    try {
      EmploymentStatus newStatus = EmploymentStatus.valueOf(status.toUpperCase());
      employee.setStatus(newStatus);
      Employee updatedEmployee = employeeRepository.save(employee);
      return employeeMapper.employeeToEmployeeDto(updatedEmployee);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("유효하지 않은 고용 상태입니다: " + status);
    }
  }

  @Override
  @Transactional
  public void deleteEmployee(Long id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("부서를 찾을 수 없습니다: " + id)); // 예외 클래스 변경

    // 직원의 부서가 있으면 직원 수 감소
    if (employee.getDepartment() != null) {
      departmentRepository.decrementEmployeeCount(employee.getDepartment().getId());
    }

    // 직원 삭제 시 이력 생성 코드 추가
    changeLogService.createChangeLogForDeletion(employee);

    // 기존 삭제 로직
    employeeRepository.delete(employee);
  }

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseEmployeeDto getEmployeesByDepartment (Long departmentId, Long idAfter, int size) {

    Pageable pageable = PageRequest.of(0, size + 1, Sort.by(Sort.Direction.ASC, "id"));

    List<Employee> employees = employeeRepository.findEmployeesByDepartment(departmentId, idAfter, pageable);

    boolean hasNext = employees.size() > size;

    if (hasNext) {
      employees = employees.subList(0, size);
    }

    Long nextIdAfter = hasNext
        ? employees.get(employees.size() - 1).getId()
        : null;

    List<EmployeeDto> employeeDtos = employees.stream()
        .map(employeeMapper::employeeToEmployeeDto)
        .collect(Collectors.toList());

    long totalElements = employeeRepository.countByDepartmentId(departmentId);

    return new CursorPageResponseEmployeeDto(
        employeeDtos,
        nextIdAfter != null ? nextIdAfter.toString() : null,
        nextIdAfter,
        size,
        totalElements,
        hasNext
    );
  }

  @Override
  @Transactional(readOnly = true)
  public long countEmployees(String status, LocalDate fromDate, LocalDate toDate) {
    if (toDate == null) {
      toDate = LocalDate.now();
    }
    return trendRepository.findEmployeeCountByDate(toDate)-trendRepository.findTotalResigned(toDate)+trendRepository.findTotalReturned(toDate);
  }

  @Override
  @Transactional(readOnly = true)
  public List<EmployeeDistributionDto> getEmployeeDistribution(String groupBy) {

    // 전체 직원 수
    long totalEmployees = employeeRepository.count();

    List<EmployeeDistributionDto> result = new ArrayList<>();

    if (totalEmployees == 0) {
      return result;
    }

    // 그룹화 기준에 따라 분포 조회
    switch (groupBy.toLowerCase()) {
      case "department":
        // 부서별 분포
        List<Object[]> departmentStats = employeeRepository.countEmployeesByDepartment();
        for (Object[] stat : departmentStats) {
          String departmentName = (String) stat[0];
          Long count = (Long) stat[1];
          double percentage = (double) count / totalEmployees * 100;

          result.add(EmployeeDistributionDto.builder()
              .groupKey(departmentName)
              .count(count)
              .percentage(Math.round(percentage * 10.0) / 10.0) // 소수점 첫째자리까지만 표시
              .build());
        }
        break;

      case "position":
        // 직급별 분포
        List<Object[]> positionStats = employeeRepository.countEmployeesByPosition();
        for (Object[] stat : positionStats) {
          String position = (String) stat[0];
          Long count = (Long) stat[1];
          double percentage = (double) count / totalEmployees * 100;

          result.add(EmployeeDistributionDto.builder()
              .groupKey(position)
              .count(count)
              .percentage(Math.round(percentage * 10.0) / 10.0)
              .build());
        }
        break;

      case "status":
        // 고용 상태별 분포
        List<Object[]> statusStats = employeeRepository.countEmployeesByStatus();
        for (Object[] stat : statusStats) {
          String status = (String) stat[0];
          Long count = (Long) stat[1];
          double percentage = (double) count / totalEmployees * 100;

          result.add(EmployeeDistributionDto.builder()
              .groupKey(status)
              .count(count)
              .percentage(Math.round(percentage * 10.0) / 10.0)
              .build());
        }
        break;

      default:
        throw new IllegalArgumentException("지원되지 않는 그룹화 기준입니다: " + groupBy);
    }

    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public List<EmployeeTrendDto> findTrends(LocalDate from, LocalDate to, String unit) {
    if (to == null) {
      to = LocalDate.now();
    }
    if (from == null) {
      from = calculateFrom(to, unit);
    }

    List<EmployeeTrendDto> trends = new ArrayList<>();
    int previousCount = trendRepository.findEmployeeCountByDate(from);
    LocalDate previousDate = null;

    List<LocalDate> dateRange = generateTrendDateRange(from, to, unit);
    for (LocalDate date : dateRange) {
      int count = trendRepository.findEmployeeCountByDate(date);
      int resigns = (previousDate == null) ? 0 : trendRepository.findResigned(previousDate.plusDays(1), date);
      int returns = (previousDate == null) ? 0 : trendRepository.findReturned(previousDate.plusDays(1), date);

      count = count  - resigns + returns;
      int change = count - previousCount;
      double changeRate = (previousCount == 0) ? 0.0 : ((double) change / previousCount) * 100;
      changeRate = Math.round(changeRate * 10.0) / 10.0;
      trends.add(new EmployeeTrendDto(date, count, change, changeRate));
      previousCount = count;
      previousDate = date;
    }
    return trends;
  }

  private String generateEmployeeNumber() {
    int currentYear = LocalDate.now().getYear();
    String yearPrefix = "EMP-" + currentYear + "-";

    // 해당 연도의 마지막 사번을 조회
    String yearPattern = yearPrefix + "%";
    Optional<Employee> latestEmployee = employeeRepository.findTopByEmployeeNumberLikeOrderByEmployeeNumberDesc(yearPattern);

    int sequence = 1;
    if (latestEmployee.isPresent()) {
      // 마지막 사번에서 시퀀스 번호 추출
      String latestNumber = latestEmployee.get().getEmployeeNumber();
      String sequencePart = latestNumber.substring(yearPrefix.length());
      sequence = Integer.parseInt(sequencePart) + 1;
    }

    // 숫자를 3자리 형식으로 포맷팅 (예: 1 -> 001)
    return yearPrefix + String.format("%03d", sequence);
  }
  private List<LocalDate> generateTrendDateRange(LocalDate from, LocalDate to, String unit) {
    List<LocalDate> dates = new ArrayList<>();
    dates.add(from);

    switch (unit) {
      case "day":
        dates.addAll(IntStream.iterate(1, i -> from.plusDays(i).isBefore(to) || from.plusDays(i).isEqual(to), i -> i + 1)
                .mapToObj(from::plusDays)
                .toList());
        break;
      case "month":
        dates.addAll(IntStream.iterate(1, i -> from.plusMonths(i).isBefore(to) || from.plusMonths(i).isEqual(to), i -> i + 1)
                .mapToObj(from::plusMonths)
                .map(date -> date.withDayOfMonth(date.lengthOfMonth()))
                .toList());
        break;
      case "quarter":
        dates.addAll(IntStream.iterate(1, i -> getQuarterEndDate(from.plusMonths(i * 3L)).isBefore(to) || getQuarterEndDate(from.plusMonths(i * 3L)).isEqual(to), i -> i + 1)
                .mapToObj(i -> getQuarterEndDate(from.plusMonths(i * 3L)))
                .toList());
        break;
      case "year":
        dates.addAll(IntStream.iterate(1, i -> from.plusYears(i).isBefore(to) || from.plusYears(i).isEqual(to), i -> i + 1)
                .mapToObj(from::plusYears)
                .map(date -> LocalDate.of(date.getYear(), 12, 31))
                .toList());
        break;
      default:
        throw new IllegalArgumentException("지원되지 않는 단위입니다: " + unit);
    }

    if (!dates.contains(to)) {
      dates.add(to);
    }

    return dates;
  }

  private LocalDate getQuarterEndDate(LocalDate date) {
    return switch (date.getMonthValue()) {
      case 1, 2, 3 -> LocalDate.of(date.getYear(), 3, 31);
      case 4, 5, 6 -> LocalDate.of(date.getYear(), 6, 30);
      case 7, 8, 9 -> LocalDate.of(date.getYear(), 9, 30);
      default -> LocalDate.of(date.getYear(), 12, 31);
    };
  }

  private LocalDate calculateFrom(LocalDate to, String unit) {
      return switch (unit.toLowerCase()) {
          case "day" -> to.minusDays(12);
          case "week" -> to.minusWeeks(12);
          case "month" -> to.minusMonths(12).withDayOfMonth(to.minusMonths(12).lengthOfMonth());
          case "quarter" -> getQuarterEndDate(to.minusMonths(36));
          case "year" -> LocalDate.of(to.getYear() - 12, 12, 31);
          default -> to.minusMonths(12);
      };
  }

}
