package com.codeit.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUpdateDescription is a Querydsl query type for UpdateDescription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUpdateDescription extends EntityPathBase<UpdateDescription> {

    private static final long serialVersionUID = -1659946902L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUpdateDescription updateDescription = new QUpdateDescription("updateDescription");

    public final StringPath after = createString("after");

    public final StringPath before = createString("before");

    public final QChangeLog changeLog;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath propertyName = createString("propertyName");

    public QUpdateDescription(String variable) {
        this(UpdateDescription.class, forVariable(variable), INITS);
    }

    public QUpdateDescription(Path<? extends UpdateDescription> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUpdateDescription(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUpdateDescription(PathMetadata metadata, PathInits inits) {
        this(UpdateDescription.class, metadata, inits);
    }

    public QUpdateDescription(Class<? extends UpdateDescription> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.changeLog = inits.isInitialized("changeLog") ? new QChangeLog(forProperty("changeLog")) : null;
    }

}

