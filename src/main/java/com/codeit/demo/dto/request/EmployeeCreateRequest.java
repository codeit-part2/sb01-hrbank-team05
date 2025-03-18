package com.codeit.demo.dto.request;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;


public record EmployeeCreateRequest(
    @NotBlank(message = "Employee name is required")
    String name,
    @NotBlank(message = "Email is required")
    String email,
    @NotBlank(message = "Department is required")
    Long departmentId,
    @NotBlank(message = "Position is required")
    String position,
    @NotBlank(message = "HireDate is required")
    LocalDate hireDate,
    String memo
) {

}