package com.codeit.demo.dto.data;

import java.util.List;
import lombok.Getter;

@Getter
public class CursorPageResponseDepartmentDto {
  private List<DepartmentDto> content;
  private Object nextCursor;
  private Long nextIdAfter;
  private int size;
  private long totalElements;
  private boolean hasNext;

  public CursorPageResponseDepartmentDto(List<DepartmentDto> content, String nextCursor,
      Long nextIdAfter, int size, long totalElements, boolean hasNext) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.nextIdAfter = nextIdAfter;
    this.size = size;
    this.totalElements = totalElements;
    this.hasNext = hasNext;
  }
}
