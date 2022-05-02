package com.pet.comes.repository.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pet.comes.model.Entity.schedule.Feed;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
}
