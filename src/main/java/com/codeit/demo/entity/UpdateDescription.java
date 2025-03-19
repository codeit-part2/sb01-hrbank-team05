package com.codeit.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Table(name = "change_description")
public class UpdateDescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "log_id")
  private ChangeLog changeLog;

  @Column(name = "property_name", length = 255)
  private String propertyName;

  @Column(length = 255)
  private String before;

  @Column(length = 255)
  private String after;

  // Getters and Setters
}
