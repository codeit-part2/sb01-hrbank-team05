package com.codeit.demo;

import com.codeit.demo.dto.EmployeeTrendDto;
import com.codeit.demo.entity.Employee;
import com.codeit.demo.entity.enums.EmploymentStatus;
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
import java.util.List;


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
        Employee employee1 = new Employee("Alice", "alice@example.com", "E1", LocalDate.of(2023, 1, 1), EmploymentStatus.ACTIVE);
        Employee employee2 = new Employee("Bob", "bob@example.com", "E2", LocalDate.of(2023, 1, 5), EmploymentStatus.ACTIVE);
        Employee employee3 = new Employee("Charlie", "charlie@example.com", "E3", LocalDate.of(2023, 1, 7), EmploymentStatus.ACTIVE);
        Employee employee4 = new Employee("Emma", "emma@example.com", "E5", LocalDate.of(2023, 1, 13), EmploymentStatus.ACTIVE);
        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
        employeeRepository.save(employee3);
        employeeRepository.save(employee4);
        em.flush(); // 데이터 저장 즉시 반영
        em.clear(); // 영속성 컨텍스트 초기화
    }

    @Test
    void testFindTrend() {

        LocalDate changeDate = LocalDate.of(2023, 1, 9);
        Employee alice = employeeRepository.findByName("Alice").orElseThrow();
        alice.setStatus(EmploymentStatus.ON_LEAVE);
        employeeRepository.save(alice);  // 변경된 상태 저장


        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 1, 13);
        String unit = "day";

        List<EmployeeTrendDto> result = employeeService.findTrends(from, to, unit);


        System.out.println("=== Employee Trend Data ===");
        for (EmployeeTrendDto dto : result) {
            System.out.printf("날짜: %s, 직원 수: %d, 변화량: %d, 변화율: %.2f%% %n",
                    dto.getDate(), dto.getCount(), dto.getChange(), dto.getChangeRate());
        }
    }


}
