package com.codeit.demo.mapper;

import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.entity.Department;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring",
    imports = {LocalDate.class, LocalDateTime.class})
public interface DepartmentMapper {

  /**
   * Department 엔티티를 DepartmentDto로 변환합니다.
   *
   * @param department 변환할 부서 엔티티
   * @return 변환된 부서 DTO
   */
  @Mapping(target = "establishedDate", expression = "java(department.getEstablishedDate() != null ? department.getEstablishedDate().atStartOfDay() : null)")
  DepartmentDto toDto(Department department);

  /**
   * DepartmentCreateRequest를 Department 엔티티로 변환합니다.
   *
   * @param request 변환할 생성 요청 객체
   * @return 변환된 부서 엔티티
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "employeeCount", constant = "0")
  Department toEntity(DepartmentCreateRequest request);

  /**
   * DepartmentUpdateRequest를 사용하여 Department 엔티티를 업데이트합니다.
   *
   * @param department 업데이트할 부서 엔티티
   * @param request 업데이트 요청 객체
   */
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "employeeCount", ignore = true)
  void updateEntityFromRequest(DepartmentUpdateRequest request, @MappingTarget Department department);
}