package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pet.comes.model.Entity.schedule.Etc;
import com.pet.comes.model.Entity.schedule.Salon;

@Repository
public interface EtcRepository extends JpaRepository<Etc, Long> {
}
