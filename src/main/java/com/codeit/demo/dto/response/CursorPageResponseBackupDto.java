package com.codeit.demo.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CursorPageResponseBackupDto<T> {
  private List<T> content; // 조회된 데이터 목록
  private String nextCursor; // 다음 페이지 커서
  private Long nextIdAfter; // 다음 페이지 ID
  private int size; // 페이지 크기
  private long totalElements; // 전체 요소 수
  private boolean hasNext; // 다음 페이지 존재 여부

  public CursorPageResponseBackupDto(List<T> content, String nextCursor, Long nextIdAfter, int size, long totalElements, boolean hasNext) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.nextIdAfter = nextIdAfter;
    this.size = size;
    this.totalElements = totalElements;
    this.hasNext = hasNext;
  }
}