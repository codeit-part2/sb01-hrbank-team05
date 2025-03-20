package com.codeit.demo.mapper;

import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import com.codeit.demo.entity.Department;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-20T18:11:26+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class DepartmentMapperImpl implements DepartmentMapper {

    @Override
    public DepartmentDto toDto(Department department) {
        if ( department == null ) {
            return null;
        }

        DepartmentDto.DepartmentDtoBuilder departmentDto = DepartmentDto.builder();

        departmentDto.id( department.getId() );
        departmentDto.name( department.getName() );
        departmentDto.description( department.getDescription() );
        departmentDto.establishedDate( department.getEstablishedDate() );
        departmentDto.employeeCount( department.getEmployeeCount() );

        return departmentDto.build();
    }

    @Override
    public Department toEntity(DepartmentCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Department department = new Department();

        department.setName( request.name() );
        department.setDescription( request.description() );
        department.setEstablishedDate( request.establishedDate() );

        department.setEmployeeCount( 0 );

        return department;
    }

    @Override
    public void updateEntityFromRequest(DepartmentUpdateRequest request, Department department) {
        if ( request == null ) {
            return;
        }

        department.setName( request.name() );
        department.setDescription( request.description() );
        department.setEstablishedDate( request.establishedDate() );
    }
}
