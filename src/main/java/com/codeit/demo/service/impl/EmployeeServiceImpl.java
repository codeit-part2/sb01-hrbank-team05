package com.codeit.demo.service.impl;

import com.codeit.demo.dto.EmployeeTrendDto;
import com.codeit.demo.repository.EmployeeRepository;
import com.codeit.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        int currentCount=findTotalEmployeeCount(to);
        LocalDate previousDate= calculatePreviousDate(to, unit);
        int previousTotal = findTotalEmployeeCount(previousDate);

        int change=currentCount-previousTotal;
        double changeRate = (previousTotal == 0) ? 0 : ((double) change / previousTotal) * 100;

        List<EmployeeTrendDto> result = trend.stream().map(obj -> new EmployeeTrendDto(
                (LocalDate) obj[0],
                ((Number) obj[1]).intValue(), change, changeRate)).toList();
        return result;
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
    private LocalDate calculatePreviousDate(LocalDate to,String unit) {
        return switch (unit.toLowerCase()) {
            case "day" -> to.minusDays(1);
            case "week" -> to.minusWeeks(1);
            case "month" -> to.minusMonths(1).withDayOfMonth(1);
            case "year" -> to.minusYears(1).with(TemporalAdjusters.firstDayOfYear());
            case "quarter" -> {
                int quarterStart = ((to.getMonthValue() - 1) / 3) * 3 + 1;
                yield to.withMonth(quarterStart).minusMonths(3).with(TemporalAdjusters.firstDayOfMonth());
            }
            default -> to.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        };
    }
    private int findTotalEmployeeCount(LocalDate date){
        Integer count = employeeRepository.findTotalCountByDate(date);
        return count !=null ? count : 0;
    }
}
