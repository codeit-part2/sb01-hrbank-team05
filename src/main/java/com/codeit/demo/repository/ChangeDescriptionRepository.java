package com.codeit.demo.repository;

import com.codeit.demo.entity.ChangeDescription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeDescriptionRepository extends
    JpaRepository<ChangeDescription, Long> {

  @Query("SELECT cd from ChangeDescription cd WHERE cd.changeLog.id = :logId")
  List<ChangeDescription> findAllByLogId(Long logId);
}
