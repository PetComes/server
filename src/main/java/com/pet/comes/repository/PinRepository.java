package com.pet.comes.repository;

import com.pet.comes.model.Entity.Diary;
import com.pet.comes.model.Entity.Pin;
import com.pet.comes.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PinRepository extends JpaRepository<Pin,Long> {

    Optional<Pin> findByUserAndDiaryId(User user,Long diary);
    List<Pin> findAllByDiaryId(Long diaryId);
    int countPinByDiaryIdAndPinedAtBetween(Long diaryId, LocalDateTime start, LocalDateTime end);

}
