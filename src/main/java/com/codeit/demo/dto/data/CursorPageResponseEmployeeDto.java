package com.codeit.demo.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "직원 목록 페이징 응답")
public class CursorPageResponseEmployeeDto {
  private List<EmployeeDto> content;
  private Object nextCursor;
  private Long nextIdAfter;
  private int size;
  private long totalElements;
  private boolean hasNext;

  public CursorPageResponseEmployeeDto(List<EmployeeDto> content, String nextCursor, Long nextIdAfter,
      int size, long totalElements, boolean hasNext) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.nextIdAfter = nextIdAfter;
    this.size = size;
    this.totalElements = totalElements;
    this.hasNext = hasNext;
  }
}