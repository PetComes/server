package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.Drug;

public interface DrugRepository extends JpaRepository<Drug, Long> {
}
