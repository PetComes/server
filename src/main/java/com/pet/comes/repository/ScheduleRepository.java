package com.pet.comes.repository;

import com.pet.comes.model.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    List<Schedule> findByUserIdAndRegisteredAtBetween(Long userId, LocalDateTime start, LocalDateTime end);
}
