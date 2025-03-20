package com.codeit.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChangeDescription is a Querydsl query type for ChangeDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChangeDescription extends EntityPathBase<ChangeDescription> {

    private static final long serialVersionUID = 848844579L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChangeDescription changeDescription = new QChangeDescription("changeDescription");

    public final StringPath after = createString("after");

    public final StringPath before = createString("before");

    public final QChangeLog changeLog;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<com.codeit.demo.entity.enums.PropertyName> propertyName = createEnum("propertyName", com.codeit.demo.entity.enums.PropertyName.class);

    public QChangeDescription(String variable) {
        this(ChangeDescription.class, forVariable(variable), INITS);
    }

    public QChangeDescription(Path<? extends ChangeDescription> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChangeDescription(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChangeDescription(PathMetadata metadata, PathInits inits) {
        this(ChangeDescription.class, metadata, inits);
    }

    public QChangeDescription(Class<? extends ChangeDescription> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.changeLog = inits.isInitialized("changeLog") ? new QChangeLog(forProperty("changeLog")) : null;
    }

}

