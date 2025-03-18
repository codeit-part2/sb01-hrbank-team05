package com.codeit.demo.dto.data;

import com.codeit.demo.entity.enums.PropertyName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "직원 정보 수정 이력 변경 내용(상세 조회용)")
@Getter
public class DiffDto {

  private String propertyName;
  private String before;
  private String after;

  public DiffDto(PropertyName propertyName, String before, String after) {
    this.propertyName = propertyName.getPropertyName();
    this.before = before;
    this.after = after;
  }
}