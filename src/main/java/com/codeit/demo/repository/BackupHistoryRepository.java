package com.codeit.demo.repository;

import com.codeit.demo.entity.BackupHistory;
import com.codeit.demo.entity.enums.BackupHistoryStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupHistoryRepository extends JpaRepository<BackupHistory, Long> {
  Optional<BackupHistory> findTopByStatusOrderByEndTimeDesc(String status);
  Optional<BackupHistory> findTopByStatusOrderByEndedAtDesc(BackupHistoryStatus status);

  @Query("SELECT b FROM BackupHistory b WHERE " +
      "(:worker IS NULL OR b.worker LIKE %:worker%) AND " +
      "(:status IS NULL OR b.backupHistoryStatus = :status) AND " +
      "(:startedAtFrom IS NULL OR b.startedAt >= :startedAtFrom) AND " +
      "(:startedAtTo IS NULL OR b.startedAt <= :startedAtTo)")
  Page<BackupHistory> findByFilters(
      @Param("worker") String worker,
      @Param("status") BackupHistoryStatus status,
      @Param("startedAtFrom") LocalDateTime startedAtFrom,
      @Param("startedAtTo") LocalDateTime startedAtTo,
      Pageable pageable);

  @Query("SELECT b FROM BackupHistory b WHERE " +
      "b.id > :idAfter AND " +
      "(:worker IS NULL OR b.worker LIKE %:worker%) AND " +
      "(:status IS NULL OR b.backupHistoryStatus = :status) AND " +
      "(:startedAtFrom IS NULL OR b.startedAt >= :startedAtFrom) AND " +
      "(:startedAtTo IS NULL OR b.startedAt <= :startedAtTo)")
  Page<BackupHistory> findByIdAfterAndFilters(
      @Param("idAfter") Long idAfter,
      @Param("worker") String worker,
      @Param("status") BackupHistoryStatus status,
      @Param("startedAtFrom") LocalDateTime startedAtFrom,
      @Param("startedAtTo") LocalDateTime startedAtTo,
      Pageable pageable);
}
