package com.codeit.demo.service;

import com.codeit.demo.entity.BackupHistory;

public interface BackupHistoryService {

  public boolean isBackupNeeded();
  public BackupHistory startBackup(String worker);
  public void performBackup(String worker);

}
