package com.codeit.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String employeeNumber;
    private LocalDate hireDate;
    private Status status;

    public Employee(String name, String email,LocalDate hireDate) {
        this.name = name;
        this.email = email;
        this.employeeNumber="E1";
        this.hireDate = hireDate;
        this.status=Status.ON;
    }
}
