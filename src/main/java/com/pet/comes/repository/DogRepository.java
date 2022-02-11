package com.pet.comes.repository;


import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.Family;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DogRepository extends JpaRepository<Dog,Long> {

    List<Dog> findAllByFamily(Family family);
}
