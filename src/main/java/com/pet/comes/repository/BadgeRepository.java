package com.pet.comes.repository;

import com.pet.comes.model.Entity.ActivatedBadge;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BadgeRepository extends CrudRepository<ActivatedBadge, Long> {
    List<ActivatedBadge> findAllByUserId(Long userId);
    Optional<ActivatedBadge> findAllByUserIdAndBadgeId(Long userId, Long badgeId);
}
