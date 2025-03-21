package com.codeit.demo.repository;

import com.codeit.demo.entity.Department;

import java.util.List;

public interface DepartmentRepositoryCustom {
    List<Department> findDepartments(Long idAfter, String nameOrDescription, String sortField, String sortDirection, int size);
}