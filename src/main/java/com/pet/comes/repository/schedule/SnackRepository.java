package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Snack;

public interface SnackRepository extends JpaRepository<Snack, Long> {
}
