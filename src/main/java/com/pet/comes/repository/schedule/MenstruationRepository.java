package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Menstruation;

public interface MenstruationRepository extends JpaRepository<Menstruation, Long> {
}
