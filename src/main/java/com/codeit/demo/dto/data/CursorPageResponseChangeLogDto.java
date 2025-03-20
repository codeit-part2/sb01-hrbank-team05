package com.codeit.demo.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "커서 기반 페이지 응답")
public class CursorPageResponseChangeLogDto<T> {

  private List<T> content;
  private Object nextCursor;
  private Long nextIdAfter;
  private int size;
  private Long totalElements;
  private boolean hasNext;

  public CursorPageResponseChangeLogDto(List<T> content, Object nextCursor, Long nextIdAfter,
      int size,
      Long totalElements, boolean hasNext) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.nextIdAfter = nextIdAfter;
    this.size = size;
    this.totalElements = totalElements;
    this.hasNext = hasNext;
  }
}