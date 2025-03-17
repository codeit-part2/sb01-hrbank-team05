package com.codeit.demo.service.impl;

import com.codeit.demo.dto.EmployeeTrendDto;
import com.codeit.demo.repository.EmployeeRepository;
import com.codeit.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeTrendDto> findTrend(LocalDate from, LocalDate to, String unit) {
        if(to==null) {
            to=LocalDate.now();
        }
        if(from==null) {
            from=calculateStart(to,unit);
        }
        List<Object[]> trend = employeeRepository.findTrend(from, to);
        return trend.stream().map(obj->new EmployeeTrendDto(
                (LocalDate) obj[0],
                ((Number) obj[1]).intValue(),
                0,
                0.0)).toList();
    }

    private LocalDate calculateStart(LocalDate to, String unit) {
        switch (unit.toLowerCase()) {
            case "day":
                return to.minusDays(12);
            case "week":
                return to.minusWeeks(12);
            case "month":
                return to.minusMonths(12);
            case "year":
                return to.minusYears(12).with(TemporalAdjusters.firstDayOfYear());
            case "quarter":
                int quarterStart = ((to.getMonthValue() - 1) / 3) * 3 + 1;
                return to.withMonth(quarterStart).with(TemporalAdjusters.firstDayOfMonth());
            default:
                return to.minusMonths(12).with(TemporalAdjusters.firstDayOfMonth());

        }
    }
}
