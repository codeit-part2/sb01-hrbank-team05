package com.codeit.demo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBinaryContent is a Querydsl query type for BinaryContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBinaryContent extends EntityPathBase<BinaryContent> {

    private static final long serialVersionUID = -420418129L;

    public static final QBinaryContent binaryContent = new QBinaryContent("binaryContent");

    public final StringPath fileFormat = createString("fileFormat");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Integer> fileSize = createNumber("fileSize", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBinaryContent(String variable) {
        super(BinaryContent.class, forVariable(variable));
    }

    public QBinaryContent(Path<? extends BinaryContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBinaryContent(PathMetadata metadata) {
        super(BinaryContent.class, metadata);
    }

}

