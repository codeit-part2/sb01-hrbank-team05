package com.codeit.demo.service;

import com.codeit.demo.entity.Backup;

public interface BackupService {

  public boolean isBackupNeeded();
  public Backup startBackup(String worker);
  public void performBackup(String worker);

}
