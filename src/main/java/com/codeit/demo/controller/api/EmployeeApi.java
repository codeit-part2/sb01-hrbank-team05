package com.codeit.demo.controller.api;

import com.codeit.demo.dto.data.EmployeeDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import com.codeit.demo.dto.response.CursorPageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "직원 관리", description = "직원 관리 API")
public interface EmployeeApi {

  @Operation(summary = "직원 목록 조회", description = "직원 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = CursorPageResponse.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees")
  ResponseEntity<CursorPageResponse<EmployeeDto>> getAllEmployees(
      @Parameter(description = "직원 이름 또는 이메일") @RequestParam(required = false) String nameOrEmail,
      @Parameter(description = "사원 번호") @RequestParam(required = false) String employeeNumber,
      @Parameter(description = "부서 이름") @RequestParam(required = false) String departmentName,
      @Parameter(description = "직함") @RequestParam(required = false) String position,
      @Parameter(description = "입사일 시작") @RequestParam(required = false) String hireDateFrom,
      @Parameter(description = "입사일 종료") @RequestParam(required = false) String hireDateTo,
      @Parameter(description = "상태 (ACTIVE, ON_LEAVE, RESIGNED)") @RequestParam(required = false) String status,
      @Parameter(description = "이전 페이지 마지막 요소 ID") @RequestParam(required = false) Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)") @RequestParam(required = false) String cursor,
      @Parameter(description = "페이지 크기 (기본값: 10)") @RequestParam(required = false, defaultValue = "10") Integer size,
      @Parameter(description = "정렬 필드 (name, employeeNumber, hireDate)") @RequestParam(required = false, defaultValue = "name") String sortField,
      @Parameter(description = "정렬 방향 (asc 또는 desc, 기본값: asc)") @RequestParam(required = false, defaultValue = "asc") String sortDirection
  );

  @Operation(summary = "직원 등록", description = "새로운 직원을 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "등록 성공",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이메일",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "/api/employees", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<EmployeeDto> createEmployee(
      @Parameter(description = "직원 정보", required = true) @RequestPart("employee") EmployeeCreateRequest employee,
      @Parameter(description = "프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile
  );

  @Operation(summary = "직원 상세 조회", description = "직원 상세 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees/{id}")
  ResponseEntity<EmployeeDto> getEmployeeById(
      @Parameter(description = "직원 ID", required = true) @PathVariable("id") Long id
  );

  @Operation(summary = "직원 수정", description = "직원 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이메일",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "직원 또는 부서를 찾을 수 없음",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PatchMapping(value = "/api/employees/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<EmployeeDto> updateEmployee(
      @Parameter(description = "직원 ID", required = true) @PathVariable("id") Long id,
      @Parameter(description = "직원 수정 정보", required = true) @RequestPart("employee") EmployeeUpdateRequest employee,
      @Parameter(description = "프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile
  );

  @Operation(summary = "직원 삭제", description = "직원을 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(mediaType = "*/*",
              schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping("/api/employees/{id}")
  ResponseEntity<Void> deleteEmployee(
      @Parameter(description = "직원 ID", required = true) @PathVariable("id") Long id
  );
}