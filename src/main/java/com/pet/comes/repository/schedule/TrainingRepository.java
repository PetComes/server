package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Training;

public interface TrainingRepository extends JpaRepository<Training, Long> {
}
