package com.codeit.demo.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class CursorPageResponse<T> {
  private final List<T> content;
  private final Long nextCursor;
  private final boolean hasNext; // 추가

  public CursorPageResponse(List<T> content, Long nextCursor) {
    this(content, nextCursor, false);
  }

  public CursorPageResponse(List<T> content, Long nextCursor, boolean hasNext) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.hasNext = hasNext; // 필드에 할당
  }
}
