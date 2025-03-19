package com.codeit.demo.service.impl;

import com.codeit.demo.dto.response.BackupHistoryDto;
import com.codeit.demo.dto.response.CursorPageResponseBackupDto;
import com.codeit.demo.entity.BackupHistory;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.BackupHistoryStatus;
import com.codeit.demo.repository.BackupHistoryRepository;
import com.codeit.demo.repository.EmployeeRepository;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BackupHistoryServiceImpl {
  private final BackupHistoryRepository backupHistoryRepository;
  private final EmployeeRepository employeeRepository;
  private String backupDirectory = "./hrbank05/file/backup";

  //자동 배치 시스템 시간 단위
  @Value("${backup.batch.interval}")
  private long backupInterval;

  @Scheduled(fixedRateString = "${backup.batch.interval}")
  public void scheduledBackup() {
    String worker = "system"; // 작업자는 system으로 고정
    performBackup(worker);
  }

  // 1. 데이터 백업 필요 여부를 판단
  public boolean isBackupNeeded() {
    // 가장 최근 완료된 백업 이력 조회
    Optional<BackupHistory> lastBackup = backupHistoryRepository.findTopByStatusOrderByEndTimeDesc("완료");

    if (lastBackup.isPresent()) {
      LocalDateTime lastBackupTime = lastBackup.get().getEndedAt();
      // 직원 데이터 변경 여부 확인 (예: 직원 테이블에서 lastBackupTime 이후 변경된 데이터가 있는지 확인)
      // return employeeRepository.existsByLastModifiedTimeAfter(lastBackupTime); // 마지막 변경 변경이력 조회 - 성삼님께 요청
    }
    return true;
  }

  // 2. 데이터 백업 필요 시 데이터 백업 이력을 등록
  public BackupHistory startBackup(String worker) { //worker ip주소 어떻게 얻을 건지 고민하기
    BackupHistory history = new BackupHistory();
    history.setWorker(worker);
    history.setStartedAt(LocalDateTime.now());
    history.setStatus(BackupHistoryStatus.IN_PROGRESS);
    return backupHistoryRepository.save(history);
  }

  // 3. 백업 생성
  public void performBackup(String worker) {
    // 백업 필요 여부 X
    if (!isBackupNeeded()) {
      BackupHistory skippedHistory = new BackupHistory();
      skippedHistory.setWorker(worker);
      skippedHistory.setStartedAt(LocalDateTime.now());
      skippedHistory.setStatus(BackupHistoryStatus.SKIP);
      backupHistoryRepository.save(skippedHistory); // 필요없다면 스킵으로 저장 및 종료
      return;
    }

    // 백업 필요 여부 O, 새로운 백업 생성
    BackupHistory history = startBackup(worker);

    try {
      // 백업 파일 생성
      File backupFile = new File(backupDirectory + "/backup_" + LocalDateTime.now().toString() + ".csv");
      List<String> employeeData = fetchEmployeeDataInChunks(); // 청크 단위로 데이터 조회
      FileUtils.writeLines(backupFile, employeeData);


      byte[] fileData = FileUtils.readFileToByteArray(backupFile);
      Long fileId = FileService.storeFile(fileData, backupFile.getName()); //파일 저장 및 fileID 획득 소율님 코드 참고하기

      // 백업 이력 업데이트
      history.setStatus(BackupHistoryStatus.COMPLETED);
      history.setEndedAt(LocalDateTime.now());
      history.setFileId(fileId);
    } catch (IOException e) {
      // 백업 실패 시 처리
      history.setStatus(BackupHistoryStatus.FAILED);
      history.setEndedAt(LocalDateTime.now());
      File errorLog = new File(backupDirectory + "/error_" + LocalDateTime.now().toString() + ".log");
      try {
        FileUtils.writeStringToFile(errorLog, e.getMessage(), "UTF-8");
        byte[] errorLogData = FileUtils.readFileToByteArray(errorLog);
        Long fileId = FileService.storeFile(errorLogData, errorLog.getName()); // 77line 동일
        history.setFileId(fileId); // fileId 저장
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    } finally {
      backupHistoryRepository.save(history);
    }
  }

  private List<String> fetchEmployeeDataInChunks() {
    List<String> allData = new ArrayList<>();
    int pageSize = 1000; // 청크 크기
    int page = 0;

    while (true) {
      Pageable pageable = PageRequest.of(page, pageSize);
      Page<Employee> employeePage = employeeRepository.findAll(pageable);
      List<String> chunkData = employeePage.getContent().stream()
          .map(employee -> employee.toCsvString()) // Employee 엔티티를 CSV 문자열로 변환
          .collect(Collectors.toList());

      allData.addAll(chunkData);

      if (employeePage.isLast()) {
        break;
      }
      page++;
    }

    return allData;
  }

  public CursorPageResponseBackupDto<BackupHistoryDto> getBackupHistory(
      String worker, BackupHistoryStatus status, LocalDateTime startedAtFrom, LocalDateTime startedAtTo,
      Long idAfter, String cursor, int size, String sortField, String sortDirection) {

    // 정렬 설정
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Sort sort = Sort.by(direction, sortField);

    // 페이징 설정
    Pageable pageable = PageRequest.of(0, size, sort);

    // 커서 기반 페이지네이션 (idAfter 또는 cursor 사용)
    Page<BackupHistory> backupHistoryPage;
    if (idAfter != null) {
      backupHistoryPage = backupHistoryRepository.findByIdAfterAndFilters(
          idAfter, worker, status, startedAtFrom, startedAtTo, pageable);
    } else if (cursor != null) {
      Long cursorId = Long.parseLong(cursor);
      backupHistoryPage = backupHistoryRepository.findByIdAfterAndFilters(
          cursorId, worker, status, startedAtFrom, startedAtTo, pageable);
    } else {
      backupHistoryPage = backupHistoryRepository.findByFilters(
          worker, status, startedAtFrom, startedAtTo, pageable);
    }

    // DTO 변환
    List<BackupHistoryDto> content = backupHistoryPage.getContent().stream()
        .map(BackupHistoryDto::new)
        .collect(Collectors.toList());

    // 다음 페이지 정보 계산
    boolean hasNext = backupHistoryPage.hasNext();
    Long nextIdAfter = hasNext ? backupHistoryPage.getContent().get(backupHistoryPage.getContent().size() - 1).getId() : null;
    String nextCursor = hasNext ? String.valueOf(nextIdAfter) : null;

    return new CursorPageResponseBackupDto<>(
        content,
        nextCursor,
        nextIdAfter,
        size,
        backupHistoryPage.getTotalElements(),
        hasNext
    );
  }

  public BackupHistoryDto getLatestBackup(BackupHistoryStatus status) {
    // 상태가 지정되지 않으면 COMPLETED 상태로 설정
    if (status == null) {
      status = BackupHistoryStatus.COMPLETED;
    }

    // 지정된 상태의 가장 최근 백업 정보 조회
    Optional<BackupHistory> latestBackup = backupHistoryRepository.findTopByStatusOrderByEndedAtDesc(status);

    // 백업 정보가 있으면 DTO로 변환하여 반환
    return latestBackup.map(BackupHistoryDto::new).orElse(null);
  }
}


/*
* 백업 DB에서 생성, 백업 데이터는 파일 DB에 저장
* 경로는 로컬 hrbank_backups 저장
* */