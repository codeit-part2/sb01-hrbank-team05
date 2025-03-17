package com.codeit.demo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EmployeeUpdateRequest(
    @NotBlank(message = "Name is required")
    String name,
    @NotBlank(message = "Email is required")
    String email,
    @NotBlank(message = "Department is required")
    Integer departmentId,
    @NotBlank(message = "Position is required")
    String position,
    @NotBlank(message = "HireDate is required")
    String hireDate,
    @NotBlank(message = "Status is required")
    String status,
    String memo
) {

}
