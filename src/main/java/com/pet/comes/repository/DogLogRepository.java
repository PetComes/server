package com.pet.comes.repository;

import com.pet.comes.model.Entity.DogLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogLogRepository extends JpaRepository<DogLog, Long> {

}
