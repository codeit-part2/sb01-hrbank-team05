package com.codeit.demo.mapper;

import com.codeit.demo.dto.data.EmployeeDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.entity.Department;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.EmploymentStatus;
import java.time.LocalDate;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-20T18:11:26+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.12.1.jar, environment: Java 17.0.14 (Oracle Corporation)"
)
@Component
public class EmployeeMapperImpl implements EmployeeMapper {

    @Override
    public EmployeeDto employeeToEmployeeDto(Employee employee) {
        if ( employee == null ) {
            return null;
        }

        EmployeeDto.EmployeeDtoBuilder employeeDto = EmployeeDto.builder();

        employeeDto.departmentName( employeeDepartmentName( employee ) );
        employeeDto.departmentId( employeeDepartmentId( employee ) );
        if ( employee.getStatus() != null ) {
            employeeDto.status( employee.getStatus().name() );
        }
        employeeDto.id( employee.getId() );
        employeeDto.name( employee.getName() );
        employeeDto.email( employee.getEmail() );
        employeeDto.employeeNumber( employee.getEmployeeNumber() );
        employeeDto.position( employee.getPosition() );
        employeeDto.hireDate( employee.getHireDate() );

        return employeeDto.build();
    }

    @Override
    public Employee employeeCreateRequestToEmployee(EmployeeCreateRequest request) {
        if ( request == null ) {
            return null;
        }

        Employee employee = new Employee();

        employee.setDepartment( departmentIdToDepartment( request.departmentId() ) );
        employee.setHireDate( request.hireDate() );
        employee.setName( request.name() );
        employee.setEmail( request.email() );
        employee.setPosition( request.position() );

        employee.setStatus( EmploymentStatus.ACTIVE );

        return employee;
    }

    @Override
    public void updateEmployeeFromRequest(EmployeeUpdateRequest request, Employee employee) {
        if ( request == null ) {
            return;
        }

        employee.setDepartment( departmentIdToDepartment( request.departmentId() ) );
        employee.setHireDate( request.hireDate() );
        employee.setStatus( request.status() );
        employee.setName( request.name() );
        employee.setEmail( request.email() );
        employee.setPosition( request.position() );
    }

    private String employeeDepartmentName(Employee employee) {
        Department department = employee.getDepartment();
        if ( department == null ) {
            return null;
        }
        return department.getName();
    }

    private Long employeeDepartmentId(Employee employee) {
        Department department = employee.getDepartment();
        if ( department == null ) {
            return null;
        }
        return department.getId();
    }
}
