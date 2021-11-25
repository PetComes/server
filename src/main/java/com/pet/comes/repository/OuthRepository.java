package com.pet.comes.repository;

import com.pet.comes.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OuthRepository extends JpaRepository<User,Long> {

    User findByEmail(String email);

    void deleteByEmail(String email);
}
