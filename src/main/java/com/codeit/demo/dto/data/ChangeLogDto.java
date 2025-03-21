package com.codeit.demo.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Getter;

@Schema(description = "직원 정보 수정 이력(목록 조회용)")
@Getter
public class ChangeLogDto {

  private Long id;
  private String type;
  private String employeeNumber;
  private String memo;
  private String ipAddress;
  private Instant at;

  public ChangeLogDto(Long id, String type, String employeeNumber, String memo, String ipAddress,
      Instant at) {
    this.id = id;
    this.type = type;
    this.employeeNumber = employeeNumber;
    this.memo = memo;
    this.ipAddress = ipAddress;
    this.at = at;
  }
}