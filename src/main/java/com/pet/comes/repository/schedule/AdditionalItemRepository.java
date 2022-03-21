package com.pet.comes.repository.schedule;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pet.comes.model.Entity.schedule.AdditionalItem;
import com.pet.comes.model.Entity.schedule.Bath;
import com.pet.comes.model.Entity.schedule.Etc;

@Repository
public interface AdditionalItemRepository extends JpaRepository<AdditionalItem, Long> {
	List<AdditionalItem> findAdditionalItemsByEtc(Etc etc);
}
