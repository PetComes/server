package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pet.comes.model.Entity.schedule.EtcItem;

public interface EtcItemRepository extends JpaRepository<EtcItem, Long> {
}
