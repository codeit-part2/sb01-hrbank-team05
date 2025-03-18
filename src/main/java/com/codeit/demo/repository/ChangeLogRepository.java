package com.codeit.demo.repository;

import com.codeit.demo.entity.ChangeLog;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {

  long countAllByAtGreaterThanAndAtLessThan(Instant fromDate, Instant toDate);

  @Query(value = "SELECT * FROM change_log cl " +
      "WHERE (:employeeNumber IS NULL OR cl.employee_number LIKE CONCAT('%', CAST(:employeeNumber AS TEXT), '%')) "
      +
      "AND (:ipAddress IS NULL OR cl.ip_address LIKE CONCAT('%', CAST(:ipAddress AS TEXT), '%')) "
      +
      "AND (:memo IS NULL OR cl.memo LIKE CONCAT('%', CAST(:memo AS TEXT), '%')) "
      +
      "AND (:type IS NULL OR :type = 'ALL' OR CAST(cl.type AS TEXT) = :type) "
      // NULL 인식 못하는 경우가 있어 type=null인 경우 ALL로 바꿔 전달하고 모든 행 select
      +
      "AND (CAST(:atFrom AS TIMESTAMP) IS NULL OR cl.at >= :atFrom) "
      // Instant 데이터 타입 인식 못하는 문제로 CAST
      +
      "AND (CAST(:atTo AS TIMESTAMP) IS NULL OR cl.at <= :atTo) "
      , nativeQuery = true)
  Page<ChangeLog> findAll(
      @Param("employeeNumber") String employeeNumber,
      @Param("ipAddress") String ipAddress,
      @Param("memo") String memo,
      @Param("type") String type, // 자료형 인식 문제로 String으로 변환하여 전달
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      Pageable pageable);


  @Query(value = "SELECT * FROM change_log cl " +
      "WHERE (:employeeNumber IS NULL OR cl.employee_number LIKE CONCAT('%', CAST(:employeeNumber AS TEXT), '%')) "
      +
      "AND (:ipAddress IS NULL OR cl.ip_address LIKE CONCAT('%', CAST(:ipAddress AS TEXT), '%')) "
      +
      "AND (:memo IS NULL OR cl.memo LIKE CONCAT('%', CAST(:memo AS TEXT), '%')) "
      +
      "AND (:type IS NULL OR :type = 'ALL' OR CAST(cl.type AS TEXT) = :type) "
      // NULL 인식 못하는 경우가 있어 type=null인 경우 ALL로 바꿔 전달하고 모든 행 select
      +
      "AND (CAST(:atFrom AS TIMESTAMP) IS NULL OR cl.at >= :atFrom) "
      // Instant 데이터 타입 인식 못하는 문제로 CAST
      +
      "AND (CAST(:atTo AS TIMESTAMP) IS NULL OR cl.at <= :atTo) "
      +
      "AND (cl.ip_address < :cursor OR (cl.ip_address = :cursor AND cl.id > :idAfter)) "
      , nativeQuery = true)
  Page<ChangeLog> findAllWithCursorIpAddressDesc(
      @Param("employeeNumber") String employeeNumber,
      @Param("type") String type,
      @Param("memo") String memo,
      @Param("ipAddress") String ipAddress,
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      @Param("idAfter") Long idAfter,
      @Param("cursor") String cursor,
      Pageable pageable
  );

  @Query(value = "SELECT * FROM change_log cl " +
      "WHERE (:employeeNumber IS NULL OR cl.employee_number LIKE CONCAT('%', CAST(:employeeNumber AS TEXT), '%')) "
      +
      "AND (:ipAddress IS NULL OR cl.ip_address LIKE CONCAT('%', CAST(:ipAddress AS TEXT), '%')) "
      +
      "AND (:memo IS NULL OR cl.memo LIKE CONCAT('%', CAST(:memo AS TEXT), '%')) "
      +
      "AND (:type IS NULL OR :type = 'ALL' OR CAST(cl.type AS TEXT) = :type) "
      // NULL 인식 못하는 경우가 있어 type=null인 경우 ALL로 바꿔 전달하고 모든 행 select
      +
      "AND (CAST(:atFrom AS TIMESTAMP) IS NULL OR cl.at >= :atFrom) "
      // Instant 데이터 타입 인식 못하는 문제로 CAST
      +
      "AND (CAST(:atTo AS TIMESTAMP) IS NULL OR cl.at <= :atTo) "
      +
      "AND (cl.ip_address > :cursor OR (cl.ip_address = :cursor AND cl.id > :idAfter)) "
      , nativeQuery = true)
  Page<ChangeLog> findAllWithCursorIpAddressAsc(
      @Param("employeeNumber") String employeeNumber,
      @Param("type") String type,
      @Param("memo") String memo,
      @Param("ipAddress") String ipAddress,
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      @Param("idAfter") Long idAfter,
      @Param("cursor") String cursor,
      Pageable pageable
  );

  @Query(value = "SELECT * FROM change_log cl " +
      "WHERE (:employeeNumber IS NULL OR cl.employee_number LIKE CONCAT('%', CAST(:employeeNumber AS TEXT), '%')) "
      +
      "AND (:ipAddress IS NULL OR cl.ip_address LIKE CONCAT('%', CAST(:ipAddress AS TEXT), '%')) "
      +
      "AND (:memo IS NULL OR cl.memo LIKE CONCAT('%', CAST(:memo AS TEXT), '%')) "
      +
      "AND (:type IS NULL OR :type = 'ALL' OR CAST(cl.type AS TEXT) = :type) "
      // NULL 인식 못하는 경우가 있어 type=null인 경우 ALL로 바꿔 전달하고 모든 행 select
      +
      "AND (CAST(:atFrom AS TIMESTAMP) IS NULL OR cl.at >= :atFrom) "
      // Instant 데이터 타입 인식 못하는 문제로 CAST
      +
      "AND (CAST(:atTo AS TIMESTAMP) IS NULL OR cl.at <= :atTo) "
      +
      "AND (cl.at < :cursor OR (cl.at=:cursor AND cl.id > :idAfter)) "
      , nativeQuery = true)
  Page<ChangeLog> findAllWithCursorAtDesc(
      @Param("employeeNumber") String employeeNumber,
      @Param("type") String type,
      @Param("memo") String memo,
      @Param("ipAddress") String ipAddress,
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      @Param("idAfter") Long idAfter,
      @Param("cursor") Instant cursor,
      Pageable pageable
  );

  @Query(value = "SELECT * FROM change_log cl " +
      "WHERE (:employeeNumber IS NULL OR cl.employee_number LIKE CONCAT('%', CAST(:employeeNumber AS TEXT), '%')) "
      +
      "AND (:ipAddress IS NULL OR cl.ip_address LIKE CONCAT('%', CAST(:ipAddress AS TEXT), '%')) "
      +
      "AND (:memo IS NULL OR cl.memo LIKE CONCAT('%', CAST(:memo AS TEXT), '%')) "
      +
      "AND (:type IS NULL OR :type = 'ALL' OR CAST(cl.type AS TEXT) = :type) "
      // NULL 인식 못하는 경우가 있어 type=null인 경우 ALL로 바꿔 전달하고 모든 행 select
      +
      "AND (CAST(:atFrom AS TIMESTAMP) IS NULL OR cl.at >= :atFrom) "
      // Instant 데이터 타입 인식 못하는 문제로 CAST
      +
      "AND (CAST(:atTo AS TIMESTAMP) IS NULL OR cl.at <= :atTo) "
      +
      "AND (cl.at > :cursor OR (cl.at=:cursor AND cl.id > :idAfter)) "
      , nativeQuery = true)
  Page<ChangeLog> findAllWithCursorAtAsc(
      @Param("employeeNumber") String employeeNumber,
      @Param("type") String type,
      @Param("memo") String memo,
      @Param("ipAddress") String ipAddress,
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      @Param("idAfter") Long idAfter,
      @Param("cursor") Instant cursor,
      Pageable pageable
  );
}
