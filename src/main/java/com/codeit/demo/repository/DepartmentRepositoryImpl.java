package com.codeit.demo.repository;

import com.codeit.demo.entity.Department;
import com.codeit.demo.entity.QDepartment;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class DepartmentRepositoryImpl implements DepartmentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public DepartmentRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Department> findDepartments(Long idAfter, String nameOrDescription, String sortField, String sortDirection, int size) {
        QDepartment department = QDepartment.department;

        return queryFactory
                .selectFrom(department)
                .where(
                        idAfter == null ? null : department.id.gt(idAfter),
                        nameOrDescription == null ? null :
                                department.name.contains(nameOrDescription).or(department.description.contains(nameOrDescription))
                )
                .orderBy(getOrderSpecifier(sortField, sortDirection))
                .limit(size)
                .fetch();
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortField, String sortDirection) {
        QDepartment department = QDepartment.department;
        Order order = "desc".equalsIgnoreCase(sortDirection) ? Order.DESC : Order.ASC;
        if ("name".equals(sortField)) {
            return new OrderSpecifier<>(order, department.name);
        } else if ("establishedDate".equals(sortField)) {
            return new OrderSpecifier<>(order, department.establishedDate);
        }
        return new OrderSpecifier<>(order, department.id);
    }
}