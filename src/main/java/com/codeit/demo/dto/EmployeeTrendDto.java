package com.codeit.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class EmployeeTrendDto {
    private LocalDate date;
    private int count;
    private int change;
    private double changeRate;
}
