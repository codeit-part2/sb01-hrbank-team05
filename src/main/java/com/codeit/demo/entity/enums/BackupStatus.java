package com.codeit.demo.entity.enums;

public enum BackupStatus {
  IN_PROGRESS("진행중"),
  COMPLETED("완료"),
  FAILED("실패"),
  SKIP("건너뜀");

  private final String koreanStatus;

  BackupStatus(String koreanStatus) {
    this.koreanStatus = koreanStatus;
  }

  @Override
  public String toString() {
    return koreanStatus;
  }
}
