package com.codeit.demo.repository;

import com.codeit.demo.entity.enums.EmploymentStatus;
import com.codeit.demo.entity.enums.PropertyName;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codeit.demo.entity.QChangeDescription.changeDescription;
import static com.codeit.demo.entity.QEmployee.employee;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmployeeStatsRepository {
    private final JPAQueryFactory query;

    public int findEmployeeCountByDate(LocalDate date) {
        Long result = query
                .select(employee.count())
                .from(employee)
                .where(employee.hireDate.loe(date))
                .fetchOne();
        return result != null ? result.intValue() : 0;
    }

    public int findResigned(LocalDate start, LocalDate end) {
        Instant startInstant = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        Long result = query
                .select(changeDescription.count())
                .from(changeDescription)
                .where(changeDescription.propertyName.eq(PropertyName.STATUS)
                        .and(changeDescription.before.ne("RESIGNED"))
                        .and(changeDescription.after.eq("RESIGNED"))
                        .and(changeDescription.changeLog.at.goe(startInstant))
                        .and(changeDescription.changeLog.at.loe(endInstant)))
                .fetchOne();

        return result != null ? result.intValue() : 0;
    }

    public int findReturned(LocalDate start, LocalDate end) {
        Instant startInstant = start.atStartOfDay(ZoneId.systemDefault()).toInstant();
        Instant endInstant = end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        Long result = query
                .select(changeDescription.count())
                .from(changeDescription)
                .where(changeDescription.propertyName.eq(PropertyName.STATUS)
                        .and(changeDescription.before.eq("RESIGNED"))
                        .and(changeDescription.after.in("ACTIVE", "ON_LEAVE"))
                        .and(changeDescription.changeLog.at.goe(startInstant))
                        .and(changeDescription.changeLog.at.loe(endInstant)))
                .fetchOne();

        return result != null ? result.intValue() : 0;
    }

    public int findTotalResigned(LocalDate end) {
        Instant endInstant = end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        Long result = query
                .select(changeDescription.count())
                .from(changeDescription)
                .where(changeDescription.propertyName.eq(PropertyName.STATUS)
                        .and(changeDescription.after.eq("RESIGNED"))
                        .and(changeDescription.changeLog.at.loe(endInstant)))
                .fetchOne();

        return result != null ? result.intValue() : 0;
    }

    public int findTotalReturned(LocalDate end) {
        Instant endInstant = end.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant();

        Long result = query
                .select(changeDescription.count())
                .from(changeDescription)
                .where(changeDescription.propertyName.eq(PropertyName.STATUS)
                        .and(changeDescription.before.eq("RESIGNED"))
                        .and(changeDescription.after.in("ACTIVE", "ON_LEAVE"))
                        .and(changeDescription.changeLog.at.loe(endInstant)))
                .fetchOne();

        return result != null ? result.intValue() : 0;
    }

    public Map<String, Integer> findEmployeeCountByDepartment(LocalDate date) {
        List<Tuple> results = query
                .select(employee.department.name, employee.count())
                .from(employee)
                .where(employee.hireDate.loe(date)
                        .and(employee.status.ne(EmploymentStatus.RESIGNED)))
                .groupBy(employee.department.name)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(employee.department.name),
                        tuple -> Optional.ofNullable(tuple.get(employee.count()))
                                .map(Number::intValue)
                                .orElse(0)
                ));
    }


    public Map<String, Integer> findCurrentCountByPosition(LocalDate date) {
        List<Tuple> results = query
                .select(employee.position, employee.count())
                .from(employee)
                .where(employee.hireDate.loe(date),
                        employee.status.ne(EmploymentStatus.RESIGNED))
                .groupBy(employee.position)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(employee.position),
                        tuple -> Optional.ofNullable(tuple.get(employee.count()))
                                .map(Number::intValue)
                                .orElse(0)
                ));
    }


}

