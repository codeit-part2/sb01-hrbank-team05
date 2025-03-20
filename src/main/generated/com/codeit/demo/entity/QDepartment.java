package com.codeit.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QDepartment is a Querydsl query type for Department
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QDepartment extends EntityPathBase<Department> {

    private static final long serialVersionUID = -581669317L;

    public static final QDepartment department = new QDepartment("department");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> employeeCount = createNumber("employeeCount", Integer.class);

    public final DatePath<java.time.LocalDate> establishedDate = createDate("establishedDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public QDepartment(String variable) {
        super(Department.class, forVariable(variable));
    }

    public QDepartment(Path<? extends Department> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDepartment(PathMetadata metadata) {
        super(Department.class, metadata);
    }

}

