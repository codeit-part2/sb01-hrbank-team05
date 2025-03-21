package com.codeit.demo.entity;

import com.codeit.demo.entity.enums.BackupStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
@Table(name = "backup")
public class BackUp {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 20)
  private String worker;

  @Column(name = "started_at", nullable = false)
  private LocalDateTime startedAt;

  @Column(name = "ended_at", nullable = false)
  private LocalDateTime endedAt;

  @Column(nullable = false)
  private BackupStatus status;

  @Column(name = "file_id", nullable = false)
  private Long fileId;
}