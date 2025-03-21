package com.codeit.demo.entity.enums;

import lombok.Getter;

@Getter
public enum BackupStatus {
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  FAILED("실패"),
  SKIPPED("건너뜀");

  private final String backupStatus;

  BackupStatus(String backupStatus) {
    this.backupStatus = backupStatus;
  }
}
