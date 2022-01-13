package com.pet.comes.repository;

import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
//    Optional<User> findByUserId(Long userId);

    Optional<User> findByEmail(String email);
    Optional<User> findByNickname(String nickname);

    List<User> findAllByFamily(Family family);

    @Query("select u.nickname from User u "+
            "where u.nickname= :nickName")
        // nativeQuery 옵션을 줘야 DB에서 쿼리문을 작성하는 방식으로 작성할 수 있음.
    Optional<String> findIsUserNickNameByNickName(@Param("nickName") String nickName);
//    List<User> findAllBy()
}
