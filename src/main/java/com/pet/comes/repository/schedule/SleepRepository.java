package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Sleep;

public interface SleepRepository extends JpaRepository<Sleep, Long> {
}
