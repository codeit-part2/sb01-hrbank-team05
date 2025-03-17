package com.codeit.demo.repository;

import com.codeit.demo.entity.BinaryContent;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BinaryContentRepository extends JpaRepository<BinaryContent, Long> {

  List<BinaryContent> findByFileNameContainingIgnoreCase(String fileName);

  List<BinaryContent> findAllByIdIn(List<Long> ids);
}