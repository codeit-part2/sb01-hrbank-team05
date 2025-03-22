package com.codeit.demo.service.impl;

import static com.codeit.demo.entity.enums.BackupStatus.COMPLETED;
import static com.codeit.demo.entity.enums.BackupStatus.IN_PROGRESS;

import com.codeit.demo.dto.response.BackupHistoryDto;
import com.codeit.demo.dto.response.CursorPageResponseBackupDto;
import com.codeit.demo.entity.Backup;
import com.codeit.demo.entity.BinaryContent;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.BackupStatus;
import com.codeit.demo.repository.BackupRepository;
import com.codeit.demo.repository.EmployeeRepository;
import com.codeit.demo.storage.BinaryContentStorage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BackupServiceImpl {
  private final BackupRepository backupRepository;
  private final EmployeeRepository employeeRepository;
  private final BinaryContentServiceImpl binaryContentService;
  private String backupDirectory = "./hrBank05/backup";
  private final DateTimeFormatter fileNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

  //자동 배치 시스템 시간 단위
  @Scheduled(fixedRateString = "${backup.batch.interval}")
  public void scheduledBackup() {
    String worker = "system"; // 작업자는 system으로 고정
    performBackup(worker);
  }

  // 1. 데이터 백업 필요 여부를 판단
  public boolean isBackupNeeded() {
    // 가장 최근 완료된 백업 이력 조회
    Optional<Backup> lastBackup = backupRepository.findLastBackup(COMPLETED);

    if (lastBackup.isPresent()) {
      LocalDateTime lastBackupTime = lastBackup.get().getEndedAt();
      // 직원 데이터 변경 여부 확인 (예: 직원 테이블에서 lastBackupTime 이후 변경된 데이터가 있는지 확인)
      return employeeRepository.existsByUpdatedAtAfter(lastBackupTime); // 마지막 변경 변경이력 조회 - 성삼님께 요청
    }
    return true;
  }

  // 2. 데이터 백업 필요 시 데이터 백업 이력을 등록
  public Backup startBackup(String worker) { //worker ip주소 어떻게 얻을 건지 고민하기
    Backup history = new Backup(worker, IN_PROGRESS);
    history.setEndedAt(LocalDateTime.now());
    return backupRepository.save(history);
  }

  // 3. 백업 생성
  public BackupHistoryDto performBackup(String worker) {
    // 백업 필요 여부 X
    if (!isBackupNeeded()) {
      Backup skippedHistory = new Backup(worker, BackupStatus.SKIPPED);
      skippedHistory.setEndedAt(LocalDateTime.now());
      backupRepository.save(skippedHistory); // 필요없다면 스킵으로 저장 및 종료
      return new BackupHistoryDto(skippedHistory);
    }

    // 백업 필요 여부 O, 새로운 백업 생성
    Backup history = startBackup(worker);

    try {
      // 백업 파일 생성
      File backupFile = new File(backupDirectory + "/backup_" + LocalDate.now().toString() + ".csv");
      List<String> employeeData = fetchEmployeeDataInChunks(); // 청크 단위로 데이터 조회
      FileUtils.writeLines(backupFile, employeeData);

      byte[] fileData = FileUtils.readFileToByteArray(backupFile);
      Long fileId = binaryContentService.storeFile(fileData, backupFile.getName()); //파일 저장 및 fileID 획득

      // BinaryContent 객체를 fileId를 통해 조회
      BinaryContent binaryContent = binaryContentService.findById(fileId);
      history.setFileId(binaryContent); // BinaryContent 객체를 Backup에 설정

      // 백업 이력 업데이트
      history.setStatus(BackupStatus.COMPLETED);
      history.setEndedAt(LocalDateTime.now());
    } catch (IOException e) {
      // 백업 실패 시 처리
      history.setStatus(BackupStatus.FAILED);
      history.setEndedAt(LocalDateTime.now());

      // 에러 로그 파일 생성 (안전한 파일명 형식 사용)
      String errorTimestamp = LocalDateTime.now().format(fileNameFormatter);
      File errorLog = new File(backupDirectory + "/error_" + errorTimestamp + ".log");
      try {
        // 에러 로그 디렉토리가 없으면 생성
        File errorDir = new File(backupDirectory);
        if (!errorDir.exists()) {
          errorDir.mkdirs();
        }

        FileUtils.writeStringToFile(errorLog, e.getMessage(), "UTF-8");
        byte[] errorLogData = FileUtils.readFileToByteArray(errorLog);
        Long fileId = binaryContentService.storeFile(errorLogData, errorLog.getName()); // 동일한 방식으로 에러 로그 파일 저장
        BinaryContent binaryContent = binaryContentService.findById(fileId);
        history.setFileId(binaryContent); // BinaryContent 객체를 Backup에 설정
      } catch (IOException ex) {
        log.error("파일 저장 중 실패", ex);
      }
    } finally {
      backupRepository.save(history);
    }
    return new BackupHistoryDto(history);
  }

  private List<String> fetchEmployeeDataInChunks() {
    List<String> allData = new ArrayList<>();
    int pageSize = 1000; // 청크 크기
    int page = 0;

    while (true) {
      Pageable pageable = PageRequest.of(page, pageSize);
      Page<Employee> employeePage = employeeRepository.findAll(pageable);
      List<String> chunkData = employeePage.getContent().stream()
          .map(Employee::toCsvString) // Employee 엔티티를 CSV 문자열로 변환
          .toList();

      allData.addAll(chunkData);
      if (employeePage.isLast()) {
        break;
      }
      page++;
    }
    return allData;
  }

  public CursorPageResponseBackupDto<BackupHistoryDto> getBackupHistory(
      String worker, BackupStatus status, LocalDateTime startedAtFrom, LocalDateTime startedAtTo,
      Long idAfter, String cursor, int size, String sortField, String sortDirection) {

    // 정렬 설정
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Sort sort = Sort.by(direction, sortField);

    // 페이징 설정
    Pageable pageable = PageRequest.of(0, size, sort);

    // 커서 기반 페이지네이션 (idAfter 또는 cursor 사용)
    Page<Backup> backupHistoryPage;
    if (idAfter != null) {
      backupHistoryPage = backupRepository.findByIdAfterAndFilters(
          idAfter, worker,startedAtFrom, startedAtTo,status, pageable);
    } else if (cursor != null) {
      Long cursorId = Long.parseLong(cursor);
      backupHistoryPage = backupRepository.findByIdAfterAndFilters(
          cursorId, worker, startedAtFrom, startedAtTo,status, pageable);
    } else {
      backupHistoryPage = backupRepository.findByFilters(
          worker,startedAtFrom, startedAtTo, status, pageable);
    }

    // DTO 변환
    List<BackupHistoryDto> content = backupHistoryPage.getContent().stream()
        .map(BackupHistoryDto::new)
        .toList();

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

  public BackupHistoryDto getLatestBackup(BackupStatus status) {
    // 상태가 지정되지 않으면 COMPLETED 상태로 설정
    if (status == null) {
      status = COMPLETED;
    }

    // 지정된 상태의 가장 최근 백업 정보 조회
    Optional<Backup> latestBackup = backupRepository.findLastBackup(status);

    // 백업 정보가 있으면 DTO로 변환하여 반환
    return latestBackup.map(BackupHistoryDto::new).orElse(null);
  }
}