package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Walk;

public interface WalkRepository extends JpaRepository<Walk, Long> {
}
