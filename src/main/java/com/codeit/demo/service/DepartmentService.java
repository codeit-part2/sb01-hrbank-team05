package com.codeit.demo.service;

import com.codeit.demo.dto.data.CursorPageResponseDepartmentDto;
import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;

public interface DepartmentService {
  CursorPageResponseDepartmentDto getAllDepartments(String nameOrDescription, Long idAfter, int size, String sortField, String sortDirection);
  DepartmentDto getDepartmentById(Long id);
  DepartmentDto createDepartment(DepartmentCreateRequest request);
  DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request);
  void deleteDepartment(Long id);
  DepartmentDto updateEmployeeCount(Long departmentId);
}