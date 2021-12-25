package com.pet.comes.repository;

import com.pet.comes.model.Entity.Diary;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

    List<Diary> findAllByDogId(Long dogId);

}
