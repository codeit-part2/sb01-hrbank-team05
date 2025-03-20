package com.codeit.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBackup is a Querydsl query type for Backup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBackup extends EntityPathBase<Backup> {

    private static final long serialVersionUID = 739933483L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBackup backup = new QBackup("backup");

    public final DateTimePath<java.time.LocalDateTime> endedAt = createDateTime("endedAt", java.time.LocalDateTime.class);

    public final QBinaryContent file;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> startedAt = createDateTime("startedAt", java.time.LocalDateTime.class);

    public final EnumPath<com.codeit.demo.entity.enums.BackupStatus> status = createEnum("status", com.codeit.demo.entity.enums.BackupStatus.class);

    public final StringPath worker = createString("worker");

    public QBackup(String variable) {
        this(Backup.class, forVariable(variable), INITS);
    }

    public QBackup(Path<? extends Backup> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBackup(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBackup(PathMetadata metadata, PathInits inits) {
        this(Backup.class, metadata, inits);
    }

    public QBackup(Class<? extends Backup> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.file = inits.isInitialized("file") ? new QBinaryContent(forProperty("file")) : null;
    }

}

