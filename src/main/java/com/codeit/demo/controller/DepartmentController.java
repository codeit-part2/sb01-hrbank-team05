package com.codeit.demo.controller;

import com.codeit.demo.controller.api.DepartmentApi;
import com.codeit.demo.dto.data.CursorPageResponseDepartmentDto;
import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.service.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping({"/departments", "/api/departments"})
public class DepartmentController implements DepartmentApi {

  private final DepartmentService departmentService;
  private final HttpServletRequest httpServletRequest;

  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponseDepartmentDto> getAllDepartments(
          @RequestParam(required = false, defaultValue = "") String nameOrDescription,
          @RequestParam(defaultValue = "0") Long lastId,
          @RequestParam(defaultValue = "10") int size,
          @RequestParam(defaultValue = "id") String sortField,
          @RequestParam(defaultValue = "asc") String sortDirection) {

    log.info("getAllDepartments called with nameOrDescription={}, lastId={}, size={}, sortField={}, sortDirection={}",
            nameOrDescription, lastId, size, sortField, sortDirection);

    log.debug("Full request URL: {}", httpServletRequest.getRequestURL() + "?" + httpServletRequest.getQueryString());

    CursorPageResponseDepartmentDto response = departmentService.getAllDepartments(
            nameOrDescription, lastId, size, sortField, sortDirection);
    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) {
    DepartmentDto department = departmentService.getDepartmentById(id);
    return ResponseEntity.ok(department);
  }

  @Override
  @PostMapping
  public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
    DepartmentDto createdDepartment = departmentService.createDepartment(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdDepartment);
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDto> updateDepartment(
          @PathVariable Long id,
          @Valid @RequestBody DepartmentUpdateRequest request) {
    DepartmentDto updatedDepartment = departmentService.updateDepartment(id, request);
    return ResponseEntity.ok(updatedDepartment);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
    departmentService.deleteDepartment(id);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PutMapping("/{id}/employee-count")
  public ResponseEntity<DepartmentDto> updateEmployeeCount(@PathVariable Long id) {
    DepartmentDto updatedDepartment = departmentService.updateEmployeeCount(id);
    return ResponseEntity.ok(updatedDepartment);
  }
}