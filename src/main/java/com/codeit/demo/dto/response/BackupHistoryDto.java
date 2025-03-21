package com.codeit.demo.dto.response;

import com.codeit.demo.entity.Backup;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BackupHistoryDto {
  private Long id;
  private String worker;
  private LocalDateTime startedAt;
  private LocalDateTime endedAt;
  private String status;
  private Long fileId; // fileId로 파일 정보 관리
/*
  public BackupHistoryDto(Long id, String worker, LocalDateTime startedAt, LocalDateTime endedAt, String status, Long fileId) {
    this.id = id;
    this.worker = worker;
    this.startedAt = startedAt;
    this.endedAt = endedAt;
    this.status = status;
    this.fileId = fileId;
  }*/

   public BackupHistoryDto(Backup backupHistory) {
    this.id = backupHistory.getId();
    this.worker = backupHistory.getWorker();
    this.startedAt = backupHistory.getStartedAt();
    this.endedAt = backupHistory.getEndedAt();
    this.status = (backupHistory.getStatus()!=null)?backupHistory.getStatus().toString():null;
    this.fileId = (backupHistory.getFile() != null) ? backupHistory.getFile().getId() : null;
  }
}