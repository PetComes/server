package com.pet.comes.repository;


import com.pet.comes.model.Entity.Dog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DogRepository extends JpaRepository<Dog,Long> {

}
