package com.codeit.demo.repository;

import com.codeit.demo.entity.UpdateDescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UpdateDescriptionRepository extends JpaRepository<UpdateDescription, Long> {

}
