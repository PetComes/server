package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Play;

public interface PlayRepository extends JpaRepository<Play, Long> {
}
