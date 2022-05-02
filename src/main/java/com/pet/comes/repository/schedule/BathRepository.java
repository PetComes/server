package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pet.comes.model.Entity.schedule.Bath;

@Repository
public interface BathRepository extends JpaRepository<Bath, Long> {
}
