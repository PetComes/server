package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Salon;

public interface SalonRepository extends JpaRepository<Salon, Long> {
}
