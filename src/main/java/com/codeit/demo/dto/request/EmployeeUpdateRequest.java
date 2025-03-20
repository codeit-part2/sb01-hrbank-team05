package com.codeit.demo.dto.request;

import com.codeit.demo.entity.enums.EmploymentStatus;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record EmployeeUpdateRequest(
    @NotBlank(message = "Name is required")
    String name,
    @NotBlank(message = "Email is required")
    String email,
    @NotBlank(message = "Department is required")
    Long departmentId,
    @NotBlank(message = "Position is required")
    String position,
    @NotBlank(message = "HireDate is required")
    LocalDate hireDate,
    @NotBlank(message = "Status is required")
    EmploymentStatus status,
    String memo
) {

}
