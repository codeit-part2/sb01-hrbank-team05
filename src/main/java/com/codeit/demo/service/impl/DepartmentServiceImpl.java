package com.codeit.demo.service.impl;

import com.codeit.demo.dto.data.CursorPageResponseDepartmentDto;
import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.entity.Department;
import com.codeit.demo.exception.DepartmentNotFoundException;
import com.codeit.demo.mapper.DepartmentMapper;
import com.codeit.demo.repository.DepartmentRepository;
import com.codeit.demo.repository.EmployeeRepository;
import com.codeit.demo.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final DepartmentMapper departmentMapper;

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponseDepartmentDto getAllDepartments(
      String nameOrDescription, Long idAfter, int size, String sortField, String sortDirection) {

    Long cursorId = idAfter != null ? idAfter : 0L;
    String sortFieldValue = sortField != null ? sortField : "id";
    String sortDirectionValue = sortDirection != null ? sortDirection : "asc";

    log.info("Service processing with sortField={}, sortDirection={}", sortFieldValue, sortDirectionValue);

    List<Department> departments = departmentRepository.findDepartments(
        cursorId, nameOrDescription, sortFieldValue, sortDirectionValue, size + 1);

    boolean hasNext = departments.size() > size;
    if (hasNext) {
      departments = departments.subList(0, size);
    }

    Long nextIdAfter = hasNext ? departments.get(departments.size() - 1).getId() : null;
    String nextCursor = nextIdAfter != null ? nextIdAfter.toString() : null;

    List<DepartmentDto> departmentDtos = departments.stream()
        .map(departmentMapper::toDto)
        .toList();

    long totalElements = departmentRepository.count();

    return new CursorPageResponseDepartmentDto(
        departmentDtos, nextCursor, nextIdAfter, size, totalElements, hasNext);
  }

  @Override
  @Transactional(readOnly = true)
  public DepartmentDto getDepartmentById(Long id) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new DepartmentNotFoundException("ID가 " + id + "인 부서를 찾을 수 없습니다."));
    return departmentMapper.toDto(department);
  }

  @Override
  @Transactional
  public DepartmentDto createDepartment(DepartmentCreateRequest request) {
    Department department = departmentMapper.toEntity(request);
    department.setEmployeeCount(0);
    Department savedDepartment = departmentRepository.save(department);
    return departmentMapper.toDto(savedDepartment);
  }

  @Override
  @Transactional
  public DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new DepartmentNotFoundException("ID가 " + id + "인 부서를 찾을 수 없습니다."));
    departmentMapper.updateEntityFromRequest(request, department);
    Department updatedDepartment = departmentRepository.save(department);
    return departmentMapper.toDto(updatedDepartment);
  }

  @Override
  @Transactional
  public void deleteDepartment(Long id) {
    if (!departmentRepository.existsById(id)) {
      throw new DepartmentNotFoundException("ID가 " + id + "인 부서를 찾을 수 없습니다.");
    }
    long employeeCount = employeeRepository.countByDepartmentId(id);
    if (employeeCount > 0) {
      throw new IllegalStateException("해당 부서에 소속된 직원이 있어 삭제할 수 없습니다.");
    }
    departmentRepository.deleteById(id);
  }

  @Override
  @Transactional
  public DepartmentDto updateEmployeeCount(Long departmentId) {
    Department department = departmentRepository.findById(departmentId)
        .orElseThrow(() -> new DepartmentNotFoundException("ID가 " + departmentId + "인 부서를 찾을 수 없습니다."));
    int count = Math.toIntExact(employeeRepository.countByDepartmentId(departmentId));
    department.setEmployeeCount(count);
    Department updatedDepartment = departmentRepository.save(department);
    return departmentMapper.toDto(updatedDepartment);
  }
}