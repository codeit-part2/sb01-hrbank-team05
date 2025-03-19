package com.codeit.demo.service.impl;

import com.codeit.demo.dto.data.ChangeLogDto;
import com.codeit.demo.dto.data.CursorPageResponseChangeLogDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.entity.ChangeLog;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.ChangeType;
import com.codeit.demo.mapper.ChangeLogMapper;
import com.codeit.demo.repository.ChangeLogRepository;
import com.codeit.demo.service.ChangeDescriptionService;
import com.codeit.demo.service.ChangeLogService;
import com.codeit.demo.util.ClientInfo;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChangeLogServiceImpl implements ChangeLogService {

  private final ChangeDescriptionService changeDescriptionService;

  private final ChangeLogRepository changeLogRepository;

  private final ChangeLogMapper changeLogMapper;

  private final ClientInfo clientInfo;

  @Transactional
  @Override
  public void createChangeLogForCreation(Employee employee,
      EmployeeCreateRequest employeeCreateRequest) {
    // change_log 추가
    String ipAddress = clientInfo.getIpAddr();
    ChangeLog changeLog = changeLogRepository.save(
        new ChangeLog(ChangeType.CREATED,
            employee.getEmployeeNumber(),
            (employeeCreateRequest.memo() != null) ? employeeCreateRequest.memo()
                : "신규 직원 등록",
            ipAddress,
            Instant.now()));
    // change_description 추가
    changeDescriptionService.createChangeDescriptionForCreation(changeLog, employeeCreateRequest);
  }

  @Transactional
  @Override
  public void createChangeLogForUpdate(Employee employee,
      EmployeeUpdateRequest employeeUpdateRequest) {
    // change_log 추가
    String ipAddress = clientInfo.getIpAddr();

    ChangeLog changeLog = changeLogRepository.save(
        new ChangeLog(ChangeType.UPDATED,
            employee.getEmployeeNumber(),
            (employeeUpdateRequest.memo() != null) ? employeeUpdateRequest.memo()
                : "직원 정보 수정",
            ipAddress,
            Instant.now()));
    // change_description 추가
    changeDescriptionService.createChangeDescriptionForUpdate(changeLog, employee,
        employeeUpdateRequest);
  }

  @Transactional
  @Override
  public void createChangeLogForDeletion(Employee employee) {
    // change_log 추가
    String ipAddress = clientInfo.getIpAddr();
    ChangeLog changeLog = changeLogRepository.save(
        new ChangeLog(ChangeType.DELETED,
            employee.getEmployeeNumber(),
            "직원 삭제",
            ipAddress,
            Instant.now()));
    // change_description 추가
    changeDescriptionService.createChangeDescriptionForDeletion(changeLog, employee);
  }

  @Override
  public long countLogs(Instant fromDate, Instant toDate) {
    // 기본 값 설정
    if (fromDate == null) {
      fromDate = Instant.now().minus(7, ChronoUnit.DAYS);
    }
    if (toDate == null) {
      toDate = Instant.now();
    }
    if (fromDate.isAfter(toDate)) {
      throw new IllegalArgumentException("유효하지 않은 날짜 범위입니다.");
    }
    return changeLogRepository.countAllByAtGreaterThanAndAtLessThan(fromDate, toDate);
  }

  @Override
  public CursorPageResponseChangeLogDto<ChangeLogDto> findAll(
      String employeeNumber, ChangeType type, String memo, String ipAddress,
      Instant atFrom, Instant atTo, Long idAfter, Object cursor,
      int size, String sortField, String sortDirection) {

    // 타입 변환(enum ChangeType -> String)
    String typeStr = (type != null) ? type.toString() : null;

    // Pageable 생성
    Sort.Direction direction = Sort.Direction.fromString(sortDirection);
    Pageable pageable = PageRequest.of(0, size, Sort.by(direction, sortField));

    Page<ChangeLog> page;

    // 커서가 없는 첫 요청 처리
    if (cursor == null) {
      page = changeLogRepository.findAll(employeeNumber, ipAddress, memo, typeStr, atFrom, atTo,
          pageable);
    } else {
      // 커서 기반 조회
      switch (sortField) {
        case "at":
          Instant cursorTime = getCursorTime(cursor);
          page = changeLogRepository.findAllWithCursorAt(employeeNumber, typeStr, ipAddress, memo,
              atFrom, atTo, idAfter, cursorTime, sortDirection, pageable);
          break;

        case "ipAddress":
          String cursorIp = getCursorIpAddress(idAfter);
          page = changeLogRepository.findAllWithCursorIpAddress(employeeNumber, typeStr, memo,
              ipAddress, atFrom, atTo, idAfter, cursorIp, sortDirection, pageable);
          break;

        default:
          throw new IllegalArgumentException("Unsupported sort field: " + sortField);
      }
    }

    // 다음 페이지 커서 설정
    boolean hasNext = page.hasNext();
    Long nextIdAfter = hasNext ? page.getContent().get(page.getContent().size() - 1).getId() : null;
    Object nextCursor = hasNext
        ? ("at".equals(sortField) ? page.getContent().get(page.getContent().size() - 1).getAt()
        : nextIdAfter)
        : null;

    // DTO 변환
    List<ChangeLogDto> changeLogDtos = page.getContent().stream()
        .map(changeLogMapper::toDto)
        .toList();

    return new CursorPageResponseChangeLogDto<>(changeLogDtos, nextCursor, nextIdAfter,
        page.getSize(), page.getTotalElements(), hasNext);
  }

  // 커서 시간을 `Instant`로 변환
  private Instant getCursorTime(Object cursor) {
    return Instant.parse((String) cursor).atZone(ZoneId.of("Asia/Seoul")).toInstant();
  }

  // `idAfter` 기반으로 IP 주소 조회
  private String getCursorIpAddress(Long idAfter) {
    return changeLogRepository.findById(idAfter)
        .orElseThrow(() -> new IllegalArgumentException("Invalid cursor"))
        .getIpAddress();
  }
}
