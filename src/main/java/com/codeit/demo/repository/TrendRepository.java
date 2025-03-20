package com.codeit.demo.repository;

import com.codeit.demo.entity.enums.EmploymentStatus;
import com.codeit.demo.entity.enums.PropertyName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

import static com.codeit.demo.entity.QChangeDescription.changeDescription;
import static com.codeit.demo.entity.QEmployee.employee;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TrendRepository {
    private final JPAQueryFactory query;

    // 특정 날짜까지 재직 중인 직원 수 조회
    public int findEmployeeCountByDate(LocalDate date) {
        Long result = query
                .select(employee.count())
                .from(employee)
                .where(employee.hireDate.loe(date).and(employee.status.ne(EmploymentStatus.RESIGNED)))
                .fetchOne();
        return result != null ? result.intValue() : 0;
    }

    // 특정 기간 동안 입사한 직원 수 조회
    public int findNewHiresBetween(LocalDate start, LocalDate end) {
        Long result = query
                .select(employee.count())
                .from(employee)
                .where(employee.hireDate.between(start, end))
                .fetchOne();
        return result != null ? result.intValue() : 0;

    }

    public int findResignedBetween(LocalDate start, LocalDate end) {
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
}

