package com.codeit.demo.controller;

import com.codeit.demo.controller.api.ChangeLogApi;
import com.codeit.demo.dto.data.ChangeLogDto;
import com.codeit.demo.dto.data.CursorPageResponseChangeLogDto;
import com.codeit.demo.dto.data.DiffDto;
import com.codeit.demo.entity.enums.ChangeType;
import com.codeit.demo.service.ChangeDescriptionService;
import com.codeit.demo.service.ChangeLogService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/change-logs")
@RequiredArgsConstructor
public class ChangeLogController implements ChangeLogApi {

  private final ChangeLogService changeLogService;
  private final ChangeDescriptionService changeDescriptionService;

  @Override
  public ResponseEntity<List<DiffDto>> findDescriptionsByLogId(Long id) {
    return null;
  }

  @Override
  public ResponseEntity<CursorPageResponseChangeLogDto<ChangeLogDto>> findAll(String employeeNumber,
      ChangeType type, String memo, String ipAddress, Instant atFrom, Instant atTo, Long idAfter,
      Object cursor, int size, String sortField, String sortDirection) {
    return null;
  }

  @GetMapping("/count")
  public ResponseEntity<Long> findCount(@RequestParam(required = false) Instant fromDate,
      @RequestParam(required = false) Instant toDate) {
    long count = changeLogService.countLogs(fromDate, toDate);
    return ResponseEntity.ok(count);
  }

}
