package com.codeit.demo.repository;

import com.codeit.demo.entity.ChangeLog;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Long> {

  long countAllByAtGreaterThanAndAtLessThan(Instant fromDate, Instant toDate);
}
