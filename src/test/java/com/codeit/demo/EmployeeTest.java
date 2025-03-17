package com.codeit.demo;

import com.codeit.demo.dto.EmployeeTrendDto;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.repository.EmployeeRepository;
import com.codeit.demo.service.EmployeeService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@ContextConfiguration(classes = HrbankApplication.class)
@Transactional
@Rollback(value = false)
class EmployeeTest {

    @Autowired
    EntityManager em;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        List<Employee> employees = Arrays.asList(
                new Employee("Alice", "alice@example.com", LocalDate.of(2023, 1, 10)),
                new Employee("Bob", "bob@example.com", LocalDate.of(2023, 3, 15)),
                new Employee("Charlie", "charlie@example.com", LocalDate.of(2023, 5, 20)),
                new Employee("David", "david@example.com", LocalDate.of(2023, 8, 5)),
                new Employee("Emma", "emma@example.com", LocalDate.of(2023, 11, 25))
        );
        employeeRepository.saveAll(employees);
        em.flush(); // 데이터 저장 즉시 반영
        em.clear(); // 영속성 컨텍스트 초기화
    }

    @Test
    void testFindTrend() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        String unit = "month";

        List<EmployeeTrendDto> result = employeeService.findTrend(from, to, unit);

        // Then
        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(5);  // 예상 결과 개수 검증
        for (EmployeeTrendDto dto : result) {
            assertThat(dto.getDate()).isNotNull();  // null 여부만 체크
            assertThat(dto.getCount()).isGreaterThan(0);  // 직원 수가 0보다 큰지 검증
        }
    }
}
