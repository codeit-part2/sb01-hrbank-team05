package com.codeit.demo.controller;

import com.codeit.demo.controller.api.DepartmentApi;
import com.codeit.demo.dto.data.CursorPageResponseDepartmentDto;
import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController implements DepartmentApi {

  private final DepartmentService departmentService;

  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponseDepartmentDto> getAllDepartments(
      @RequestParam(required = false) String nameOrDescription,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(defaultValue = "10") int size) {

    CursorPageResponseDepartmentDto response =
        departmentService.getAllDepartments(nameOrDescription, idAfter, size);

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