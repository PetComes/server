package com.pet.comes.repository;

import com.pet.comes.model.Entity.Badge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeListRepository extends CrudRepository<Badge, Long> {
}
