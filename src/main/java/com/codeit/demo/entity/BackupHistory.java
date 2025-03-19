//package com.codeit.demo.entity;
//
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import java.time.LocalDateTime;
//import lombok.Getter;
//
//@Entity
//@Getter
//@Table(name = "backup")
//public class BackupHistory {
//
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private Long id;
//
//  @Column(nullable = false, length = 20)
//  private String worker;
//
//  @Column(name = "started_at", nullable = false)
//  private LocalDateTime startedAt;
//
//  @Column(name = "ended_at", nullable = false)
//  private LocalDateTime endedAt;
//
//  @Column(nullable = false)
//  private BackupHistoryStatus backupHistoryStatus;
//
//  @Column(name = "file_id", nullable = false)
//  private Long fileId;
//
//  public void setStatus(BackupHistoryStatus backupHistoryStatus) {
//    this.backupHistoryStatus = backupHistoryStatus;
//  }
//
//  public void setStartedAt(LocalDateTime startedAt) {
//    this.startedAt = startedAt;
//  }
//
//  public void setEndedAt(LocalDateTime endedAt) {
//    this.endedAt = endedAt;
//  }
//
//  public void setWorker(String worker) {
//    this.worker = worker;
//  }
//
//  public void setFileId(Long fileId) {
//    this.fileId = fileId;
//  }
//}