package com.codeit.demo.repository;

import com.codeit.demo.entity.Backup;
import com.codeit.demo.entity.enums.BackupStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupRepository extends JpaRepository<Backup, Long> {

    Optional<Backup> findTopByStatusOrderByEndedAtDesc(BackupStatus status);

    @Query("SELECT b FROM Backup b WHERE " +
        "(:worker IS NULL OR b.worker LIKE %:worker%) AND " +
        "(:status IS NULL OR b.status = :status) AND " +
        "(:startedAtFrom IS NULL OR b.startedAt >= :startedAtFrom) AND " +
        "(:startedAtTo IS NULL OR b.startedAt <= :startedAtTo)")
    Page<Backup> findByFilters(
        @Param("worker") String worker,
        @Param("status") BackupStatus status,
        @Param("startedAtFrom") LocalDateTime startedAt,
        @Param("startedAtTo") LocalDateTime endedAt,
        Pageable pageable);

    @Query("SELECT b FROM Backup b WHERE " +
        "b.id > :idAfter AND " +
        "(:worker IS NULL OR b.worker LIKE %:worker%) AND " +
        "(:status IS NULL OR b.status = :status) AND " +
        "(:startedAtFrom IS NULL OR b.startedAt >= :startedAtFrom) AND " +
        "(:startedAtTo IS NULL OR b.startedAt <= :startedAtTo)")
    Page<Backup> findByIdAfterAndFilters(
        @Param("idAfter") Long idAfter,
        @Param("worker") String worker,
        @Param("status") BackupStatus status,
        @Param("startedAtFrom") LocalDateTime startedAt,
        @Param("startedAtTo") LocalDateTime endedAt,
        Pageable pageable);
  }