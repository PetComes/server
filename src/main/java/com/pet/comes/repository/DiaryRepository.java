package com.pet.comes.repository;

import com.pet.comes.model.Entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    List<Diary> findAllByDogId(Long dogId);
    List<Diary> findByUserIdAndRegisteredAtBetween(Long UserId, LocalDateTime start, LocalDateTime end);

}
