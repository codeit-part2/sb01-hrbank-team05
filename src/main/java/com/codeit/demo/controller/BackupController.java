package com.codeit.demo.controller;

import com.codeit.demo.dto.response.BackupHistoryDto;
import com.codeit.demo.dto.response.CursorPageResponseBackupDto;
import com.codeit.demo.entity.enums.BackupStatus;
import com.codeit.demo.service.impl.BackupServiceImpl;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backups")
public class BackupController {

  @Autowired
  private BackupServiceImpl backupService;

  @PostMapping("")
  public ResponseEntity<BackupHistoryDto> createBackup() {
    String localIp;
    try {
      localIp = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    BackupHistoryDto result = backupService.performBackup(localIp);
    return ResponseEntity.ok(result);
  }


  @GetMapping("")
  public ResponseEntity<CursorPageResponseBackupDto<?>> getBackupHistory(
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) BackupStatus status,
      @RequestParam(required = false) LocalDateTime startedAtFrom,
      @RequestParam(required = false) LocalDateTime startedAtTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "startedAt") String sortField,
      @RequestParam(defaultValue = "DESC") String sortDirection) {

    CursorPageResponseBackupDto<?> response = backupService.getBackupHistory(
        worker, status, startedAtFrom, startedAtTo, idAfter, cursor, size, sortField, sortDirection);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/latest")
  public ResponseEntity<BackupHistoryDto> getLatestBackup(
      @RequestParam(required = false) BackupStatus status) {
    BackupHistoryDto latestBackup = backupService.getLatestBackup(status);
    return ResponseEntity.ok(latestBackup);
  }
}
