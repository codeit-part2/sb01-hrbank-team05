package com.codeit.demo.controller;

import com.codeit.demo.controller.api.DepartmentApi;
import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.dto.response.CursorPageResponse;
import com.codeit.demo.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/departments")
public class DepartmentController implements DepartmentApi {

  private final DepartmentService departmentService;

  @Override
  @GetMapping
  public ResponseEntity<CursorPageResponse<DepartmentDto>> getAllDepartments(
      @RequestParam(required = false) String nameOrDescription,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") Integer size,
      @RequestParam(defaultValue = "establishedDate") String sortField,
      @RequestParam(defaultValue = "asc") String sortDirection) {

    Long cursorVal = null;
    if (cursor != null && !cursor.isEmpty()) {
      try {
        cursorVal = Long.parseLong(cursor);
      } catch (NumberFormatException e) {
        // 명세서에 따라 잘못된 커서 값이면 400 에러를 반환할 수 있음
        throw new IllegalArgumentException("유효하지 않은 커서 값입니다: " + cursor);
      }
    } else if (idAfter != null) {
      cursorVal = idAfter;
    }

    CursorPageResponse<DepartmentDto> response = departmentService.getAllDepartments(
        cursorVal, size, nameOrDescription, sortField, sortDirection);

    return ResponseEntity.ok(response);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("id") Long id) {
    DepartmentDto department = departmentService.getDepartmentById(id);
    return ResponseEntity.ok(department);
  }

  @Override
  @PostMapping
  public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
    DepartmentDto createdDepartment = departmentService.createDepartment(request);
    return ResponseEntity.ok(createdDepartment); // 201 대신 200 사용
  }

  @Override
  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDto> updateDepartment(
      @PathVariable("id") Long id,
      @Valid @RequestBody DepartmentUpdateRequest request) {
    DepartmentDto updatedDepartment = departmentService.updateDepartment(id, request);
    return ResponseEntity.ok(updatedDepartment);
  }

  @Override
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDepartment(@PathVariable("id") Long id) {
    departmentService.deleteDepartment(id);
    return ResponseEntity.noContent().build();
  }
}