package com.codeit.demo.entity.enums;

import lombok.Getter;

@Getter
public enum EmploymentStatus {
  ACTIVE("ACTIVE"),
  ON_LEAVE("ON_LEAVE"),
  RESIGNED("RESIGNED");

  private String value;
  EmploymentStatus(String value) {
    this.value = value;
  }
}
