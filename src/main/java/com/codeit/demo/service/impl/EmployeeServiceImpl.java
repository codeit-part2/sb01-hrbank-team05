package com.codeit.demo.service.impl;

import com.codeit.demo.dto.EmployeeTrendDto;
import com.codeit.demo.repository.EmployeeRepository;
import com.codeit.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeTrendDto> findTrends(LocalDate from, LocalDate to, String unit) {
        if(to==null) {
            to=LocalDate.now();
        }
        if(from==null) {
            from=calculateStart(to,unit);
        }
        List<LocalDate> dateRange = generateDateRange(from, to, unit);

        List<EmployeeTrendDto> trends = new ArrayList<>();
        Integer previousCount = null;

        for (LocalDate date : dateRange) {
            int currentCount = employeeRepository.findTotalCountNoResigned(date);
            int change = (previousCount == null) ? 0 : currentCount - previousCount;
            double changeRate = (previousCount == null || previousCount == 0) ? 0.0 : ((double) change / previousCount) * 100;
            trends.add(new EmployeeTrendDto(date, currentCount, change, changeRate));
            previousCount = currentCount;
        }

        return trends;

    }



    private LocalDate calculateStart(LocalDate to, String unit) {
        return switch (unit.toLowerCase()) {
            case "day" -> to.minusDays(12);
            case "week" -> to.minusWeeks(12);
            case "month" -> to.minusMonths(12);
            case "year" -> to.minusYears(12).with(TemporalAdjusters.firstDayOfYear());
            case "quarter" -> {
                int quarterStart = ((to.getMonthValue() - 1) / 3) * 3 + 1;
                yield to.withMonth(quarterStart).with(TemporalAdjusters.firstDayOfMonth());
            }
            default -> to.minusMonths(12).with(TemporalAdjusters.firstDayOfMonth());
        };
    }

    private List<LocalDate> generateDateRange(LocalDate from, LocalDate to, String unit) {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate current = from;

        while (!current.isAfter(to)) {
            dates.add(current);
            current = switch (unit.toLowerCase()) {
                case "day" -> current.plusDays(1);
                case "week" -> current.plusWeeks(1);
                case "month" -> current.plusMonths(1);
                case "year" -> current.plusYears(1);
                case "quarter" -> current.plusMonths(3);
                default -> current.plusMonths(1);
            };
        }
        return dates;
    }
}
