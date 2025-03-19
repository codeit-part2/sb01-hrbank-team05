package com.codeit.demo.entity;

import com.codeit.demo.entity.enums.PropertyName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "change_description")
@Getter
@NoArgsConstructor
public class ChangeDescription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  @JdbcTypeCode(SqlTypes.NAMED_ENUM)
  @Column(name = "property_name", nullable = false)
  private PropertyName propertyName;

  @Column(length = 255)
  private String before;

  @Column(length = 255)
  private String after;

  @ManyToOne
  @JoinColumn(name = "log_id", nullable = false)
  private ChangeLog changeLog;

  public ChangeDescription(PropertyName propertyName, String before, String after,
      ChangeLog changeLog) {
    this.propertyName = propertyName;
    this.before = before;
    this.after = after;
    this.changeLog = changeLog;
  }

}