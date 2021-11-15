package com.pet.comes.repository;

import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    List<User> findAllByFamily(Family family);
  //  List<User> findAllBy()
}
