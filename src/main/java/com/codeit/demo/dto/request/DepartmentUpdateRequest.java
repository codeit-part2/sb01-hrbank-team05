package com.codeit.demo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DepartmentUpdateRequest(
    String name,
    String description,
    LocalDate establishedDate
) {

}
