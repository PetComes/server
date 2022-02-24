package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Hospital;

public interface HospitalRepository extends JpaRepository<Hospital, Long> {
}
