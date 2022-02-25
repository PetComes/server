package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pet.comes.model.Entity.schedule.Salon;
import com.pet.comes.model.Entity.schedule.Walk;

@Repository
public interface WalkRepository extends JpaRepository<Walk, Long> {
}
