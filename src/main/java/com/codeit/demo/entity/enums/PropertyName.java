package com.codeit.demo.entity.enums;

import lombok.Getter;

@Getter
public enum PropertyName {
  NAME("name"),
  EMAIL("email"),
  DEPARTMENT_NAME("department"),
  POSITION("position"),
  HIRE_DATE("hireDate"),
  STATUS("status");

  private String propertyName;

  PropertyName(String property) {
    this.propertyName = property;
  }
}
