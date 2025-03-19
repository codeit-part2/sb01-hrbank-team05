package com.codeit.demo.controller.api;

import com.codeit.demo.dto.data.ChangeLogDto;
import com.codeit.demo.dto.data.CursorPageResponseChangeLogDto;
import com.codeit.demo.dto.data.DiffDto;
import com.codeit.demo.dto.response.ErrorResponse;
import com.codeit.demo.entity.enums.ChangeType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.Instant;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "직원 정보 수정 이력 관리", description = "직원 정보 수정  이력 관리 API")
public interface ChangeLogApi {

  @Operation(summary = "직원 정보 수정 이력 상세 조회",
      description = "직원 정보 수정 이력의 상세 정보를 조회합니다. 변경 상세 내용이 포함됩니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(mediaType = MediaType.ALL_VALUE, array = @ArraySchema(schema = @Schema(implementation = DiffDto.class)))),
      @ApiResponse(responseCode = "404", description = "이력을 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<List<DiffDto>> findDescriptionsByLogId(
      @Parameter(description = "이력 ID") @PathVariable Long id);

  @Operation(summary = "수정 이력 건수 조회",
      description = "직원 정보 수정 이력 건수를 조회합니다. 파라미터를 제공하지 않으면 최근 일주일 데이터를 반환합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(type = "integer", format = "int64"))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 유효하지 않은 날짜 범위",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<Long> findCount(
      @Parameter(description = "시작 일시(기본값: 7일 전)") @RequestParam Instant fromDate,
      @Parameter(description = "종료 일시(기본값: 현재)") @RequestParam Instant toDate);

  @Operation(summary = "직원 정보 수정 이력 목록 조회",
      description = "직원 정보 수정 이력 목록을 조회합니다. 상세 변경 내용은 포함되지 않습니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseChangeLogDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 지원하지 않는 정렬 필드",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<CursorPageResponseChangeLogDto<ChangeLogDto>> findAll(
      @Parameter(description = "대상 직원 사번") @RequestParam(required = false) String employeeNumber,
      @Parameter(description = "이력 유형(CREATED, UPDATED, DELETED)") @RequestParam(required = false) ChangeType type,
      @Parameter(description = "내용") @RequestParam(required = false) String memo,
      @Parameter(description = "IP 주소") @RequestParam(required = false) String ipAddress,
      @Parameter(description = "수정 일시(부터)") @RequestParam(required = false) Instant atFrom,
      @Parameter(description = "수정 일시(까지)") @RequestParam(required = false) Instant atTo,
      @Parameter(description = "이전 페이지 마지막 요소 ID") @RequestParam(required = false) Long idAfter,
      @Parameter(description = "커서 (이전 페이지의 마지막 ID)") @RequestParam(required = false) Object cursor,
      @Parameter(description = "페이지 크기") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "정렬 필드(ipAddress, at)") @RequestParam(defaultValue = "at") String sortField,
      @Parameter(description = "정렬 방향(asc, desc)") @RequestParam(defaultValue = "desc") String sortDirection);
}
