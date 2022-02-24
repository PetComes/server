package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Potty;

public interface PottyRepository extends JpaRepository<Potty, Long> {
}
