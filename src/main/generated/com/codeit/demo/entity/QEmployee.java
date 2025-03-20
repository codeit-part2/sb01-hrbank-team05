package com.codeit.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmployee is a Querydsl query type for Employee
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmployee extends EntityPathBase<Employee> {

    private static final long serialVersionUID = 1478014935L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmployee employee = new QEmployee("employee");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final QDepartment department;

    public final StringPath email = createString("email");

    public final StringPath employeeNumber = createString("employeeNumber");

    public final DatePath<java.time.LocalDate> hireDate = createDate("hireDate", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath name = createString("name");

    public final StringPath position = createString("position");

    public final QBinaryContent profileImage;

    public final EnumPath<com.codeit.demo.entity.enums.EmploymentStatus> status = createEnum("status", com.codeit.demo.entity.enums.EmploymentStatus.class);

    public final DateTimePath<java.time.LocalDateTime> updatedAt = createDateTime("updatedAt", java.time.LocalDateTime.class);

    public QEmployee(String variable) {
        this(Employee.class, forVariable(variable), INITS);
    }

    public QEmployee(Path<? extends Employee> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmployee(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmployee(PathMetadata metadata, PathInits inits) {
        this(Employee.class, metadata, inits);
    }

    public QEmployee(Class<? extends Employee> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.department = inits.isInitialized("department") ? new QDepartment(forProperty("department")) : null;
        this.profileImage = inits.isInitialized("profileImage") ? new QBinaryContent(forProperty("profileImage")) : null;
    }

}

