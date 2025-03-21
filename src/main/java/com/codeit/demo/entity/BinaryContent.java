package com.codeit.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file")
public class BinaryContent {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String fileName;
  private Integer fileSize;
  private String fileFormat;

  public BinaryContent(String fileName, Integer fileSize, String fileFormat) {
    this.fileName = fileName;
    this.fileSize = fileSize;
    this.fileFormat = fileFormat;
  }

}
