package com.codeit.demo.controller;

import com.codeit.demo.dto.response.BackupHistoryDto;
import com.codeit.demo.dto.response.CursorPageResponseBackupDto;
import com.codeit.demo.entity.enums.BackupHistoryStatus;
import com.codeit.demo.service.impl.BackupHistoryServiceImpl;
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
public class BackupHistoryController {

  @Autowired
  private BackupHistoryServiceImpl backupHistoryService;

  @PostMapping("")
  public ResponseEntity<String> createBackup() {
    String localIp = null;
    try {
      localIp = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      throw new RuntimeException(e);
    }
    backupHistoryService.performBackup(localIp);
    return ResponseEntity.ok("백업 생성이 시작되었습니다.");
  }

  @GetMapping("")
  public ResponseEntity<CursorPageResponseBackupDto<?>> getBackupHistory(
      @RequestParam(required = false) String worker,
      @RequestParam(required = false) BackupHistoryStatus status,
      @RequestParam(required = false) LocalDateTime startedAtFrom,
      @RequestParam(required = false) LocalDateTime startedAtTo,
      @RequestParam(required = false) Long idAfter,
      @RequestParam(required = false) String cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "startedAt") String sortField,
      @RequestParam(defaultValue = "DESC") String sortDirection) {

    CursorPageResponseBackupDto<?> response = backupHistoryService.getBackupHistory(
        worker, status, startedAtFrom, startedAtTo, idAfter, cursor, size, sortField, sortDirection);

    return ResponseEntity.ok(response);
  }

  @GetMapping("/latest")
  public ResponseEntity<BackupHistoryDto> getLatestBackup(
      @RequestParam(required = false) BackupHistoryStatus status) {
    BackupHistoryDto latestBackup = backupHistoryService.getLatestBackup(status);
    return ResponseEntity.ok(latestBackup);
  }
}
