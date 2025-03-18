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
import org.springframework.web.bind.annotation.PathVariable;
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
  @GetMapping("/{id}/diffs")
  public ResponseEntity<List<DiffDto>> findDescriptionsByLogId(@PathVariable Long id) {
    List<DiffDto> diffDtos = changeDescriptionService.findAllChangeDescriptionsByLogId(id);
    return ResponseEntity.ok(diffDtos);
  }

  @GetMapping("")
  public ResponseEntity<CursorPageResponseChangeLogDto<ChangeLogDto>> findAll(
      @RequestParam(required = false) String employeeNumber, @RequestParam(required = false)
  ChangeType type, @RequestParam(required = false) String memo,
      @RequestParam(required = false) String ipAddress,
      @RequestParam(required = false) Instant atFrom,
      @RequestParam(required = false) Instant atTo,
      @RequestParam(required = false) Long idAfter, @RequestParam(required = false) Object cursor,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "at") String sortField,
      @RequestParam(defaultValue = "desc") String sortDirection) {
    CursorPageResponseChangeLogDto<ChangeLogDto> result = changeLogService.findAll(employeeNumber,
        type, memo
        , ipAddress, atFrom, atTo, idAfter, cursor, size, sortField, sortDirection);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/count")
  public ResponseEntity<Long> findCount(@RequestParam(required = false) Instant fromDate,
      @RequestParam(required = false) Instant toDate) {
    long count = changeLogService.countLogs(fromDate, toDate);
    return ResponseEntity.ok(count);
  }

}
