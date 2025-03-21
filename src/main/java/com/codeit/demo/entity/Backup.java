package com.codeit.demo.entity;

import com.codeit.demo.entity.enums.BackupStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Table(name = "backup")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Backup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 255, nullable = false)
  private String worker;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "ended_at", nullable = false)
  private LocalDateTime endedAt;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "status", nullable = false)
  private BackupStatus status;

  @ManyToOne (fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id")
  private BinaryContent file;

  public Backup(String worker,BackupStatus status) {
    this.worker = worker;
    this.startedAt = LocalDateTime.now();
    this.status = status;
  }

  public void setStatus(BackupStatus status) {
    this.status = status;
  }

  public void setEndedAt(LocalDateTime endedAt) {
    this.endedAt = endedAt;
  }


  public void setFileId(BinaryContent file) {
    this.file = file;
  }

}
