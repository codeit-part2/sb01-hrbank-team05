package com.codeit.demo.controller;

import com.codeit.demo.controller.api.EmployeeApi;
import com.codeit.demo.dto.data.CursorPageResponseEmployeeDto;
import com.codeit.demo.dto.data.EmployeeDistributionDto;
import com.codeit.demo.dto.data.EmployeeDto;
import com.codeit.demo.dto.data.EmployeeTrendDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.service.EmployeeService;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController implements EmployeeApi {

  private final EmployeeService employeeService;

  public EmployeeController(EmployeeService employeeService) {
    this.employeeService = employeeService;
  }

  @Override
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDto> createEmployee(
      @RequestPart("employee") EmployeeCreateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile file) throws IOException {

    EmployeeDto createdEmployee = employeeService.createEmployee(request, file);
    return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
  }

  @Override
  @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id,
      @RequestPart("employee") EmployeeUpdateRequest request,
      @RequestPart(value = "profile", required = false) MultipartFile file) throws IOException {

    // Employee 정보 업데이트 (이미지 여부와 관계없이 실행)
    EmployeeDto updatedEmployee = employeeService.updateEmployee(id, request);

    // profileImage가 제공된 경우에만 이미지 업데이트
    if (file != null && !file.isEmpty()) {
      updatedEmployee = employeeService.updateProfileImage(id, file);
    }

    return ResponseEntity.ok(updatedEmployee);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
    return ResponseEntity.ok(employeeService.getEmployeeById(id));
  }

  @GetMapping
  @Override
  public ResponseEntity<CursorPageResponseEmployeeDto> getAllEmployees(
      @RequestParam(required = false) String nameOrEmail,
      @RequestParam(required = false) String employeeNumber,
      @RequestParam(required = false) String departmentName,
      @RequestParam(required = false) String position,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateTo,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "id") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection) {

    CursorPageResponseEmployeeDto response = employeeService.findAllEmployees(
        nameOrEmail, employeeNumber, departmentName, position,
        hireDateFrom, hireDateTo, status,
        idAfter, cursor, size, sortField, sortDirection);

    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/count")
  public ResponseEntity<Long> findEmployeeCount(
      @RequestParam(required = false) String status,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireStartDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireEndDate) {

    long count = employeeService.countEmployees(status, hireStartDate, hireEndDate);
    return ResponseEntity.ok(count);
  }


  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @GetMapping("/stats/trend")
  public ResponseEntity<List<EmployeeTrendDto>> trend(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate to,
                                                      @RequestParam(defaultValue = "month") String unit){
    List<EmployeeTrendDto> result=employeeService.findTrends(from, to, unit);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/stats/distribution")
  public ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
      @RequestParam(defaultValue = "department") String groupBy,
      @RequestParam(defaultValue = "ACTIVE") String status
  ) {
    return ResponseEntity.ok(employeeService.findEmployeeDistribution(groupBy,status));
  }
}