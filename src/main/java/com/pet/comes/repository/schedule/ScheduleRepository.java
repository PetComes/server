package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
