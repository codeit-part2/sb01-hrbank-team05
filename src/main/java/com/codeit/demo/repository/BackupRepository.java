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

    @Query("SELECT b FROM Backup b WHERE CAST(b.status AS string) = :status ORDER BY b.endedAt DESC limit 1")
    Optional<Backup> findLastBackup(@Param("status") BackupStatus status);


    @Query("SELECT b FROM Backup b WHERE " +
        "(COALESCE(:worker, '') = '' OR b.worker LIKE CONCAT('%', :worker, '%')) AND " +
        "(COALESCE(:status, NULL) IS NULL OR CAST(b.status AS string) = :status) AND " +
        "(COALESCE(:startedAtFrom, NULL) IS NULL OR b.startedAt >= :startedAtFrom) AND " +
        "(COALESCE(:startedAtTo, NULL) IS NULL OR b.startedAt <= :startedAtTo)")
    Page<Backup> findByFilters(
        @Param("worker") String worker,
        @Param("startedAtFrom") LocalDateTime startedAtFrom,
        @Param("startedAtTo") LocalDateTime startedAtTo,
        @Param("status") BackupStatus status,
        Pageable pageable);


    @Query("SELECT b FROM Backup b WHERE " +
        "b.id > :idAfter AND " +
        "(:worker IS NULL OR b.worker LIKE CONCAT('%', :worker, '%')) AND " +
        "(:status IS NULL OR CAST(b.status AS string) = :status) AND " +
        "(:startedAtFrom IS NULL OR b.startedAt >= :startedAtFrom) AND " +
        "(:startedAtTo IS NULL OR b.startedAt <= :startedAtTo)")
    Page<Backup> findByIdAfterAndFilters(
        @Param("idAfter") Long idAfter,
        @Param("worker") String worker,
        @Param("startedAtFrom") LocalDateTime startedAtFrom,
        @Param("startedAtTo") LocalDateTime startedAtTo,
        @Param("status") BackupStatus status,
        Pageable pageable);
  }