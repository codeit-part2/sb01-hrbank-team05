package com.codeit.demo.repository;

import com.codeit.demo.entity.ChangeLog;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

// QueryDSL 적용 레포지토리
public interface ChangeLogCustomRepository {

  // 첫  요청 시 커서 없이 페이지네이션
  Page<ChangeLog> findAll(
      String employeeNumber,
      String ipAddress,
      String memo,
      String typeStr,
      Instant atFrom,
      Instant atTo,
      Pageable pageable);

  // IpAddress 기준 정렬 시 페이지네이션
  Page<ChangeLog> findAllWithCursorIpAddress(
      String employeeNumber,
      String type,
      String memo,
      String ipAddress,
      Instant atFrom,
      Instant atTo,
      Long idAfter,
      String cursor,
      String sortDirection,
      Pageable pageable
  );

  // at 기준 정렬 시 페이지네이션
  Page<ChangeLog> findAllWithCursorAt(
      @Param("employeeNumber") String employeeNumber,
      @Param("type") String type,
      @Param("memo") String memo,
      @Param("ipAddress") String ipAddress,
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      @Param("idAfter") Long idAfter,
      @Param("cursor") Instant cursor,
      @Param("sortDirection") String sortDirection,
      Pageable pageable
  );
}
