package com.pet.comes.repository;

import com.pet.comes.model.Entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary,Long> {

}
