package com.codeit.demo.controller.api;

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

  @Operation(summary = "직원 목록 조회", description = "직원 목록을 페이지네이션으로 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees")
  ResponseEntity<Page<EmployeeDto>> getAllEmployees(
      @Parameter(description = "직원 이름 또는 이메일") @RequestParam(required = false) String nameOrEmail,
      @Parameter(description = "사원 번호") @RequestParam(required = false) String employeeNumber,
      @Parameter(description = "부서 이름") @RequestParam(required = false) String departmentName,
      @Parameter(description = "직함") @RequestParam(required = false) String position,
      @Parameter(description = "입사일 시작") @RequestParam(required = false) LocalDate hireDateFrom,
      @Parameter(description = "입사일 종료") @RequestParam(required = false) LocalDate hireDateTo,
      @Parameter(description = "상태 (재직중, 휴직중, 퇴사)") @RequestParam(required = false) String status,
      @Parameter(description = "페이지 번호 (0부터 시작, 기본값: 0)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "페이지 크기 (기본값: 10)") @RequestParam(defaultValue = "10") int size,
      @Parameter(description = "정렬 필드 (name, employeeNumber, hireDate, 기본값: name)") @RequestParam(defaultValue = "name") String sortField,
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

  @Operation(summary = "직원 수 조회", description = "조건에 맞는 직원 수를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = Long.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees/count")
  ResponseEntity<Long> getEmployeeCount(
      @Parameter(description = "상태 (재직중, 휴직중, 퇴사)") @RequestParam(required = false) String status,
      @Parameter(description = "입사일 시작") @RequestParam(required = false) LocalDate hireStartDate,
      @Parameter(description = "입사일 종료") @RequestParam(required = false) LocalDate hireEndDate
  );

  @Operation(summary = "부서별 직원 조회", description = "부서별로 직원 목록을 페이지네이션으로 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = Page.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "404", description = "부서를 찾을 수 없음",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees/department/{departmentId}")
  ResponseEntity<Page<EmployeeDto>> getEmployeesByDepartment(
      @Parameter(description = "부서 ID") @PathVariable Long departmentId,
      @Parameter(description = "페이지 번호 (0부터 시작, 기본값: 0)") @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "페이지 크기 (기본값: 10)") @RequestParam(defaultValue = "10") int size
  );

  @Operation(summary = "직원 트렌드 조회", description = "직원 입사/퇴사 트렌드를 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "조회 성공",
          content = @Content(schema = @Schema(implementation = EmployeeTrendDto.class))),
      @ApiResponse(responseCode = "400", description = "잘못된 요청",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
      @ApiResponse(responseCode = "500", description = "서버 오류",
          content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  @GetMapping("/api/employees/stats/trend")
  ResponseEntity<List<EmployeeTrendDto>> getEmployeeTrend(
      @Parameter(description = "시작 날짜") @RequestParam(required = false) LocalDate startDate,
      @Parameter(description = "종료 날짜") @RequestParam(required = false) LocalDate endDate,
      @Parameter(description = "상태 (재직중, 휴직중, 퇴사)") @RequestParam(required = false) String status
  );
}