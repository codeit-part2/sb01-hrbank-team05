package com.codeit.demo.controller;

import com.codeit.demo.controller.api.DepartmentApi;
import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.dto.response.CursorPageResponse;
import com.codeit.demo.service.DepartmentService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // 먼저 부서 목록을 조회
    CursorPageResponse<DepartmentDto> response = departmentService.getAllDepartments(
        cursorVal, size, nameOrDescription, sortField, sortDirection);

    // 각 부서별로 직원 수를 업데이트
    List<DepartmentDto> updatedDepartments = response.getContent().stream()
        .map(dept -> departmentService.updateEmployeeCount(dept.getId()))
        .collect(Collectors.toList());

    // 업데이트된 부서 정보로 응답 객체를 새로 생성
    CursorPageResponse<DepartmentDto> updatedResponse = new CursorPageResponse<>(
        updatedDepartments,
        response.getNextCursor(),
        response.isHasNext());

    return ResponseEntity.ok(updatedResponse);
  }

  @Override
  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable("id") Long id) {
    DepartmentDto department = departmentService.updateEmployeeCount(id);
    return ResponseEntity.ok(department);
  }

  @Override
  @PostMapping
  public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
    DepartmentDto createdDepartment = departmentService.createDepartment(request);
    return ResponseEntity.ok(createdDepartment);
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