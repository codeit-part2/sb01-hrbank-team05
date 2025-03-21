package com.codeit.demo.controller.api;

import com.codeit.demo.dto.data.CursorPageResponseEmployeeDto;
import com.codeit.demo.dto.data.EmployeeDistributionDto;
import com.codeit.demo.dto.data.EmployeeDto;
import com.codeit.demo.dto.data.EmployeeTrendDto;
import com.codeit.demo.dto.request.EmployeeCreateRequest;
import com.codeit.demo.dto.request.EmployeeUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Tag(name = "직원 관리", description = "직원 관리 API")
public interface EmployeeApi {

  @Operation(summary = "직원 목록 조회", description = "직원 목록을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseEmployeeDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees")
  ResponseEntity<CursorPageResponseEmployeeDto> getAllEmployees(
      @Parameter(description = "직원 이름 또는 이메일") @RequestParam(required = false) String nameOrEmail,
      @Parameter(description = "사원 번호") @RequestParam(required = false) String employeeNumber,
      @Parameter(description = "부서 이름") @RequestParam(required = false) String departmentName,
      @Parameter(description = "직함") @RequestParam(required = false) String position,
      @Parameter(description = "입사일 시작") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateFrom,
      @Parameter(description = "입사일 종료") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hireDateTo,
      @Parameter(description = "상태 (ACTIVE, ON_LEAVE, RESIGNED)") @RequestParam(required = false) String status,
      @Parameter(description = "이전 페이지 마지막 요소 ID") @RequestParam(required = false) Long idAfter,
      @Parameter(description = "커서 (다음 페이지 시작점)") @RequestParam(required = false) Object cursor,
      @Parameter(description = "페이지 크기 (기본값: 10)") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "정렬 필드 (id, name, employeeNumber, hireDate, 기본값: id)") @RequestParam(defaultValue = "id") String sortField,
      @Parameter(description = "정렬 방향 (asc 또는 desc, 기본값: asc)") @RequestParam(defaultValue = "asc") String sortDirection
  );


  @Operation(summary = "직원 등록", description = "새로운 직원을 등록합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "등록 성공",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 중복된 이메일",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PostMapping(value = "/api/employees", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<EmployeeDto> createEmployee(
      @Parameter(description = "직원 정보") @RequestPart("employee") EmployeeCreateRequest employee,
      @Parameter(description = "프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws IOException;

  @Operation(summary = "직원 상세 조회", description = "직원 상세 정보를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees/{id}")
  ResponseEntity<EmployeeDto> getEmployeeById(
      @Parameter(description = "직원 ID") @PathVariable Long id
  );

  @Operation(summary = "직원 정보 수정", description = "직원 정보를 수정합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "수정 성공",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @PatchMapping(value = "/api/employees/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  ResponseEntity<EmployeeDto> updateEmployee(
      @Parameter(description = "직원 ID") @PathVariable Long id,
      @Parameter(description = "직원 수정 정보") @RequestPart("employee") EmployeeUpdateRequest employee,
      @Parameter(description = "프로필 이미지") @RequestPart(value = "profile", required = false) MultipartFile profile
  ) throws IOException;

  @Operation(summary = "직원 삭제", description = "직원을 삭제합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "삭제 성공"),
      @ApiResponse(responseCode = "404", description = "직원을 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @DeleteMapping("/api/employees/{id}")
  ResponseEntity<Void> deleteEmployee(
      @Parameter(description = "직원 ID") @PathVariable Long id
  );


  @Operation(summary = "직원 수 조회", description = "지정된 조건에 맞는 직원 수를 조회합니다. 상태 필터링 및 입사일 기간 필터링이 가능합니다.")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청",
                  content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees/count")
  ResponseEntity<Long> getEmployeeCount(
      @Parameter(description = "상태 (재직중, 휴직중, 퇴사)") @RequestParam(required = false) String status,
      @Parameter(description = "입사일 시작") @RequestParam(required = false) LocalDate fromDate,
      @Parameter(description = "입사일 종료") @RequestParam(required = false) LocalDate toDate
  );

  @Operation(summary = "부서별 직원 조회 (커서 기반)", description = "부서별로 직원 목록을 커서 기반으로 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = CursorPageResponseEmployeeDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees/department/{departmentId}")
  ResponseEntity<CursorPageResponseEmployeeDto> getEmployeesByDepartment(
      @Parameter(description = "부서 ID") @PathVariable Long departmentId,
      @Parameter(description = "이전 페이지 마지막 요소 ID (커서)") @RequestParam(required = false) Long idAfter,
      @Parameter(description = "페이지 크기 (기본값: 10)") @RequestParam(defaultValue = "10") int size
  );


  @Operation(summary = "직원 수 추이 조회", description = "지정된 기간 및 시간 단위로 그룹화된 직원 수 추이를 조회합니다. 파라미터를 제공하지 않으면 최근 12개월 데이터를 월 단위로 반환합니다.")
  @ApiResponses({
      @ApiResponse(responseCode = "200", description = "조회 성공"),
      @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 지원하지 않는 시간 단위",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponse.class)))

  })
  @GetMapping("/stats/trend")
  ResponseEntity<List<EmployeeTrendDto>> trend(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate from,
                                                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate to,
                                                      @RequestParam(defaultValue = "month") String unit);


  @Operation(summary = "직원 분포 조회", description = "지정된 기준으로 그룹화된 직원 분포를 조회합니다.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "조회 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청 또는 지원하지 않는 그룹화 기준",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = ErrorResponse.class))),
          @ApiResponse(responseCode = "500", description = "서버 오류",
                  content = @Content(mediaType = "application/json",
                          schema = @Schema(implementation = ErrorResponse.class)))

  })
  @GetMapping("/stats/distribution")
  ResponseEntity<List<EmployeeDistributionDto>> getEmployeeDistribution(
          @RequestParam(defaultValue = "department") String groupBy,
          @RequestParam(defaultValue = "ACTIVE") String status
  );
}


