package com.codeit.demo.controller.api;

import com.codeit.demo.dto.data.CursorPageResponseDepartmentDto;
import com.codeit.demo.dto.data.DepartmentDto;
import com.codeit.demo.dto.request.DepartmentCreateRequest;
import com.codeit.demo.dto.request.DepartmentUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@Tag(name = "부서 관리", description = "부서 관리 API")
public interface DepartmentApi {

  @Operation(summary = "부서 목록 조회", description = "부서 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseDepartmentDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/departments")
  ResponseEntity<CursorPageResponseDepartmentDto> getAllDepartments(
      @Parameter(description = "부서 이름 또는 설명") @RequestParam(required = false) String nameOrDescription,
      @Parameter(description = "이전 페이지 마지막 요소 ID (커서)") @RequestParam(required = false) Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)") @RequestParam(required = false) Object cursor,
      @Parameter(description = "페이지 크기 (기본값: 10)") @RequestParam(defaultValue = "10") int size);


  @Operation(summary = "부서 상세 조회", description = "부서 상세 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<DepartmentDto> getDepartmentById(@Parameter(description = "부서 ID") @PathVariable Long id);

  @Operation(summary = "부서 등록", description = "새로운 부서를 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "등록 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<DepartmentDto> createDepartment(@Parameter(description = "부서 정보") @RequestBody DepartmentCreateRequest request);

  @Operation(summary = "부서 정보 수정", description = "부서 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<DepartmentDto> updateDepartment(
      @Parameter(description = "부서 ID") @PathVariable Long id,
      @Parameter(description = "부서 수정 정보") @RequestBody DepartmentUpdateRequest request);

  @Operation(summary = "부서 삭제", description = "부서를 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 (소속된 직원이 있는 경우)",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<Void> deleteDepartment(@Parameter(description = "부서 ID") @PathVariable Long id);

  @Operation(summary = "부서 직원 수 업데이트", description = "부서의 직원 수를 업데이트합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "업데이트 성공",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  ResponseEntity<DepartmentDto> updateEmployeeCount(@Parameter(description = "부서 ID") @PathVariable Long id);
}