package com.codeit.demo.service.impl;

import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.dto.response.CursorPageResponse;
import com.codeit.demo.entity.Department;
import com.codeit.demo.exception.DepartmentNotFoundException;
import com.codeit.demo.mapper.DepartmentMapper;
import com.codeit.demo.repository.DepartmentRepository;
import com.codeit.demo.repository.EmployeeRepository;
import com.codeit.demo.service.DepartmentService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;
  private final DepartmentMapper departmentMapper;

  @Override
  @Transactional(readOnly = true)
  public CursorPageResponse<DepartmentDto> getAllDepartments(Long cursor, int size, String nameOrDescription,
      String sortField, String sortDirection) {
    if (size <= 0) {
      size = 10; // 기본 페이지 크기
    }

    Sort.Direction direction = "desc".equalsIgnoreCase(sortDirection) ? Sort.Direction.DESC : Sort.Direction.ASC;
    String field = "establishedDate";
    if ("name".equals(sortField)) {
      field = "name";
    }

    Sort sort = Sort.by(direction, field);
    Pageable pageable = PageRequest.of(0, size + 1, sort);

    List<Department> departments;
    if (cursor == null) {
      if (nameOrDescription != null && !nameOrDescription.isEmpty()) {
        departments = departmentRepository.findByNameContainingOrDescriptionContaining(
            nameOrDescription, nameOrDescription, pageable).getContent();
      } else {
        departments = departmentRepository.findAll(pageable).getContent();
      }
    } else {
      Department reference = departmentRepository.findById(cursor)
          .orElseThrow(() -> new DepartmentNotFoundException("부서를 찾을 수 없습니다."));

      if (nameOrDescription != null && !nameOrDescription.isEmpty()) {
        if (direction == Sort.Direction.ASC) {
          departments = departmentRepository.findByNameContainingOrDescriptionContainingAndIdGreaterThan(
              nameOrDescription, nameOrDescription, cursor, pageable).getContent();
        } else {
          departments = departmentRepository.findByNameContainingOrDescriptionContainingAndIdLessThan(
              nameOrDescription, nameOrDescription, cursor, pageable).getContent();
        }
      } else {
        if (direction == Sort.Direction.ASC) {
          departments = departmentRepository.findByIdGreaterThan(cursor, pageable).getContent();
        } else {
          departments = departmentRepository.findByIdLessThan(cursor, pageable).getContent();
        }
      }
    }

    boolean hasNext = departments.size() > size;
    List<Department> content = hasNext ? departments.subList(0, size) : departments;
    Long nextCursor = hasNext && !content.isEmpty() ? content.get(content.size() - 1).getId() : null;

    List<DepartmentDto> departmentDtos = content.stream()
        .map(departmentMapper::toDto)
        .collect(Collectors.toList());

    return new CursorPageResponse<>(departmentDtos, nextCursor, hasNext);
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
    department.setEmployeeCount(0); // 신규 부서는 직원 수 0으로 초기화
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

    // 해당 부서의 직원들이 있는지 확인
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