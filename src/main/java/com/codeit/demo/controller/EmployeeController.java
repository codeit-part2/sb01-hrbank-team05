package com.codeit.demo.controller;

import com.codeit.demo.controller.api.EmployeeApi;
import com.codeit.demo.dto.EmployeeTrendDto;
import com.codeit.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeApi {
    private final EmployeeService employeeService;

    @Override
    @GetMapping("/stats/trend")
    public ResponseEntity<List<EmployeeTrendDto>> trend(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate from,
                                                        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate to,
                                                        @RequestParam(defaultValue = "month") String unit){
        List<EmployeeTrendDto> result=employeeService.findTrend(from, to, unit);
        return ResponseEntity.ok(result);
    }


}
