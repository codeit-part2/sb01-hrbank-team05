package com.codeit.demo.repository;

import com.codeit.demo.entity.ChangeLog;
import com.codeit.demo.entity.QChangeLog;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChangeLogCustomRepositoryImpl implements ChangeLogCustomRepository {

  private final JPAQueryFactory jpaQueryFactory;

  @Override
  public Page<ChangeLog> findAll(
      String employeeNumber,
      String ipAddress,
      String memo,
      String typeStr,
      Instant atFrom,
      Instant atTo,
      Pageable pageable) {

    QChangeLog changeLog = QChangeLog.changeLog;
    BooleanBuilder builder = new BooleanBuilder();

    // 필터 조건 추가
    if (employeeNumber != null) {
      builder.and(changeLog.employeeNumber.containsIgnoreCase(employeeNumber));
    }
    if (ipAddress != null) {
      builder.and(changeLog.ipAddress.containsIgnoreCase(ipAddress));
    }
    if (memo != null) {
      builder.and(changeLog.memo.containsIgnoreCase(memo));
    }
    if (typeStr != null) {
      builder.and(changeLog.type.stringValue().eq(typeStr));
    }
    if (atFrom != null) {
      builder.and(changeLog.at.goe(atFrom));
    }
    if (atTo != null) {
      builder.and(changeLog.at.loe(atTo));
    }

    // at 또는 ipAddress에 따라 정렬
    List<OrderSpecifier<?>> orderSpecifiers = pageable.getSort().stream()
        .map(order -> {
          if (order.getProperty().equals("at")) {
            return order.isAscending() ? changeLog.at.asc() : changeLog.at.desc();
          } else if (order.getProperty().equals("ipAddress")) {
            return order.isAscending() ? changeLog.ipAddress.asc() : changeLog.ipAddress.desc();
          } else {
            return null; // 다른 필드는 받을 수 없으므로 null
          }
        })
        .filter(orderSpecifier -> orderSpecifier != null)
        .collect(Collectors.toList());

    // 기본 정렬 추가 (정렬이 없는 경우)
    if (orderSpecifiers.isEmpty()) {
      orderSpecifiers.add(changeLog.at.desc()); // 기본 정렬: 날짜 최신순
    }

    // 데이터 조회
    List<ChangeLog> results = jpaQueryFactory
        .selectFrom(changeLog)
        .where(builder)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])) // 정렬 추가
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // total elements
    long total = jpaQueryFactory
        .select(changeLog.count())
        .from(changeLog)
        .where(builder)
        .fetchOne();

    return new PageImpl<>(results, pageable, total);
  }

  @Override
  public Page<ChangeLog> findAllWithCursorIpAddress(
      String employeeNumber,
      String type,
      String memo,
      String ipAddress,
      Instant atFrom,
      Instant atTo,
      Long idAfter,
      String cursor,
      String sortDirection,
      Pageable pageable) {

    QChangeLog changeLog = QChangeLog.changeLog;
    BooleanBuilder builder = new BooleanBuilder();

    if (employeeNumber != null) {
      builder.and(changeLog.employeeNumber.containsIgnoreCase(employeeNumber));
    }
    if (ipAddress != null) {
      builder.and(changeLog.ipAddress.containsIgnoreCase(ipAddress));
    }
    if (memo != null) {
      builder.and(changeLog.memo.containsIgnoreCase(memo));
    }
    if (type != null) {
      builder.and(changeLog.type.stringValue().eq(type));
    }
    if (atFrom != null) {
      builder.and(changeLog.at.goe(atFrom));
    }
    if (atTo != null) {
      builder.and(changeLog.at.loe(atTo));
    }

    // 커서 기반 페이징
    if (cursor != null) {
      BooleanExpression cursorCondition = null;
      if ("asc".equalsIgnoreCase(sortDirection)) {
        // cl.ipAdderss > cursor OR (cl.ipAddress = ipAddress AND cl.id > idAfter)
        cursorCondition = changeLog.ipAddress.gt(cursor)
            .or(changeLog.ipAddress.eq(cursor).and(changeLog.id.gt(idAfter)));
      } else if ("desc".equalsIgnoreCase(sortDirection)) {
        // cl.ipAdderss < cursor OR (cl.ipAddress = ipAddress AND cl.id > idAfter)
        cursorCondition = changeLog.ipAddress.lt(cursor)
            .or(changeLog.ipAddress.eq(cursor).and(changeLog.id.gt(idAfter)));
      }
      if (cursorCondition != null) {
        builder.and(cursorCondition);
      }
    }

    // ipAddress 기준 정렬
    List<OrderSpecifier<?>> orderSpecifiers = pageable.getSort().stream()
        .map(order -> {
          if (order.getProperty().equals("ipAddress")) {
            return order.isAscending() ? changeLog.ipAddress.asc() : changeLog.ipAddress.desc();
          } else {
            return null;
          }
        })
        .filter(orderSpecifier -> orderSpecifier != null)
        .collect(Collectors.toList());

    if (orderSpecifiers.isEmpty()) {
      if ("asc".equalsIgnoreCase(sortDirection)) {
        orderSpecifiers.add(changeLog.ipAddress.asc());
      } else {
        orderSpecifiers.add(changeLog.ipAddress.desc());  //sortDirection 지정 안하면 desc를 기본으로
      }
    }

    List<ChangeLog> results = jpaQueryFactory
        .selectFrom(changeLog)
        .where(builder)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])) // 정렬 적용
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // total elements
    long total = jpaQueryFactory
        .select(changeLog.count())
        .from(changeLog)
        .where(builder)
        .fetchOne();

    return new PageImpl<>(results, pageable, total);
  }

  @Override
  public Page<ChangeLog> findAllWithCursorAt(
      String employeeNumber,
      String type,
      String memo,
      String ipAddress,
      Instant atFrom,
      Instant atTo,
      Long idAfter,
      Instant cursor,
      String sortDirection,
      Pageable pageable) {

    QChangeLog changeLog = QChangeLog.changeLog;
    BooleanBuilder builder = new BooleanBuilder();

    if (employeeNumber != null) {
      builder.and(changeLog.employeeNumber.containsIgnoreCase(employeeNumber));
    }
    if (ipAddress != null) {
      builder.and(changeLog.ipAddress.containsIgnoreCase(ipAddress));
    }
    if (memo != null) {
      builder.and(changeLog.memo.containsIgnoreCase(memo));
    }
    if (type != null) {
      builder.and(changeLog.type.stringValue().eq(type));
    }
    if (atFrom != null) {
      builder.and(changeLog.at.goe(atFrom));
    }
    if (atTo != null) {
      builder.and(changeLog.at.loe(atTo));
    }

    // at 커서 기반 페이징
    if (cursor != null) {
      BooleanExpression cursorCondition = null;
      if ("asc".equalsIgnoreCase(sortDirection)) {
        // cl.at > at OR (cl.at = at AND cl.id > idAfter)
        cursorCondition = changeLog.at.gt(cursor)
            .or(changeLog.at.eq(cursor).and(changeLog.id.gt(idAfter)));
      } else if ("desc".equalsIgnoreCase(sortDirection)) {
        cursorCondition = changeLog.at.lt(cursor)
            .or(changeLog.at.eq(cursor).and(changeLog.id.gt(idAfter)));
      }
      if (cursorCondition != null) {
        builder.and(cursorCondition);
      }
    }

    // 정렬 조건은 at
    List<OrderSpecifier<?>> orderSpecifiers = pageable.getSort().stream()
        .map(order -> {
          if (order.getProperty().equals("at")) {
            return order.isAscending() ? changeLog.at.asc() : changeLog.at.desc();
          } else {
            return null;
          }
        })
        .filter(orderSpecifier -> orderSpecifier != null)
        .collect(Collectors.toList());

    if (orderSpecifiers.isEmpty()) {
      if ("asc".equalsIgnoreCase(sortDirection)) {
        orderSpecifiers.add(changeLog.at.asc());
      } else {
        orderSpecifiers.add(changeLog.at.desc()); // 기본 정렬 방향 desc
      }
    }

    List<ChangeLog> results = jpaQueryFactory
        .selectFrom(changeLog)
        .where(builder)
        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0])) // 정렬 적용
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();

    // total_elements 조회
    long total = jpaQueryFactory
        .select(changeLog.count())
        .from(changeLog)
        .where(builder)
        .fetchOne();

    return new PageImpl<>(results, pageable, total);
  }
}
