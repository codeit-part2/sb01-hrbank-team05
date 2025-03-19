package com.codeit.demo.util;

import java.util.List;
import java.util.function.Function;

public class CursorPageUtil {

  public static <T, ID extends Comparable<ID>> ID getNextCursor(List<T> items, Function<T, ID> idExtractor, int size) {
    if (items == null || items.isEmpty() || items.size() < size) {
      return null; // 더 이상 데이터가 없음
    }

    // 마지막 아이템의 ID를 다음 커서로 사용
    return idExtractor.apply(items.get(items.size() - 1));
  }
}