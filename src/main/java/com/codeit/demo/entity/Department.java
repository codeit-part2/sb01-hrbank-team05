package com.codeit.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;

@Entity
@Getter
@Table(name = "department", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Department {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
