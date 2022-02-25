package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pet.comes.model.Entity.schedule.Salon;
import com.pet.comes.model.Entity.schedule.Training;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
}