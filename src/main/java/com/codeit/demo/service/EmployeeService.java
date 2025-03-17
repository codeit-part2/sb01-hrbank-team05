package com.codeit.demo.service;

import com.codeit.demo.dto.EmployeeTrendDto;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    List<EmployeeTrendDto> findTrend(LocalDate from, LocalDate to, String unit);

}
