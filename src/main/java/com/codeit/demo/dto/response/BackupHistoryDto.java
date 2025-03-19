package com.codeit.demo.dto.response;

import com.codeit.demo.entity.BackupHistory;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BackupHistoryDto {
  private Long id;
  private String worker;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  private String status;
  private Long fileId; // fileId로 파일 정보 관리

  public BackupHistoryDto(BackupHistory backupHistory) {
    this.id = backupHistory.getId();
    this.worker = backupHistory.getWorker();
    this.startTime = backupHistory.getStartedAt();
    this.endTime = backupHistory.getEndedAt();
    this.status = String.valueOf(backupHistory.getBackupHistoryStatus());
    this.fileId = backupHistory.getFileId(); // fileId 설정
  }
}