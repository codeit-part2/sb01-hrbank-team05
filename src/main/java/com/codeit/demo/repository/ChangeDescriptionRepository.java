package com.codeit.demo.repository;

import com.codeit.demo.entity.ChangeDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChangeDescriptionRepository extends JpaRepository<ChangeDescription, Long> {

}
