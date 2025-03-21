package com.codeit.demo.repository;

import com.codeit.demo.entity.ChangeLog;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {

  long countAllByAtGreaterThanAndAtLessThan(Instant fromDate, Instant toDate);
<<<<<<< HEAD

  @Query(value = "SELECT cl FROM ChangeLog cl " +
      "WHERE (:employeeNumber IS NULL OR cl.employeeNumber LIKE CONCAT('%', CAST(:employeeNumber AS string), '%')) "
      +
      "AND (:ipAddress IS NULL OR cl.ipAddress LIKE CONCAT('%', CAST(:ipAddress AS string), '%')) "
      +
      "AND (:memo IS NULL OR cl.memo LIKE CONCAT('%', CAST(:memo AS string), '%')) "
      +
      "AND (:type IS NULL OR CAST(cl.type AS string) = :type) "
      +
      "AND (CAST(:atFrom AS timestamp ) IS NULL OR cl.at >= :atFrom) "
      +
      "AND (CAST(:atTo AS timestamp) IS NULL OR cl.at <= :atTo) ")
  Page<ChangeLog> findAll(
      @Param("employeeNumber") String employeeNumber,
      @Param("ipAddress") String ipAddress,
      @Param("memo") String memo,
      @Param("type") String typeStr,
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      Pageable pageable);

  @Query(value = "SELECT cl FROM ChangeLog cl " +
      "WHERE (:employeeNumber IS NULL OR cl.employeeNumber LIKE CONCAT('%', CAST(:employeeNumber AS string), '%')) "
      +
      "AND (:ipAddress IS NULL OR cl.ipAddress LIKE CONCAT('%', CAST(:ipAddress AS string), '%')) "
      +
      "AND (:memo IS NULL OR cl.memo LIKE CONCAT('%', CAST(:memo AS string), '%')) "
      +
      "AND (:type IS NULL OR CAST(cl.type AS string) = :type) "
      +
      "AND (CAST(:atFrom AS timestamp ) IS NULL OR cl.at >= :atFrom) "
      +
      "AND (CAST(:atTo AS timestamp) IS NULL OR cl.at <= :atTo) "
      +
      "AND ("
      +
      "    (CASE WHEN :sortDirection = 'asc' THEN cl.ipAddress > :cursor "
      +
      "          WHEN :sortDirection = 'desc' THEN cl.ipAddress < :cursor " +
      "          ELSE false END) " +
      "    OR (cl.ipAddress = :cursor AND cl.id > :idAfter) " +
      ") ")
  Page<ChangeLog> findAllWithCursorIpAddress(
      @Param("employeeNumber") String employeeNumber,
      @Param("type") String type,
      @Param("memo") String memo,
      @Param("ipAddress") String ipAddress,
      @Param("atFrom") Instant atFrom,
      @Param("atTo") Instant atTo,
      @Param("idAfter") Long idAfter,
      @Param("cursor") String cursor,
      @Param("sortDirection") String sortDirection,
      Pageable pageable
  );


  @Query(value = "SELECT cl FROM ChangeLog cl " +
      "WHERE (:employeeNumber IS NULL OR cl.employeeNumber LIKE CONCAT('%', CAST(:employeeNumber AS string), '%')) "
      +
      "AND (:ipAddress IS NULL OR cl.ipAddress LIKE CONCAT('%', CAST(:ipAddress AS string), '%')) "
      +
      "AND (:memo IS NULL OR cl.memo LIKE CONCAT('%', CAST(:memo AS string), '%')) "
      +
      "AND (:type IS NULL OR CAST(cl.type AS string) = :type) "
      +
      "AND (CAST(:atFrom AS timestamp ) IS NULL OR cl.at >= :atFrom) "
      +
      "AND (CAST(:atTo AS timestamp) IS NULL OR cl.at <= :atTo) "
      +
      "AND ( "
      +
      "    (CASE WHEN :sortDirection = 'asc' THEN cl.at > :cursor "
      +
      "          WHEN :sortDirection = 'desc' THEN cl.at < :cursor " +
      "          ELSE false END) " +
      "    OR (cl.at = :cursor AND cl.id > :idAfter) " +
      ") ")
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
=======
>>>>>>> 1cce8853613b413e464d2b58596cb1de6c484397
}
