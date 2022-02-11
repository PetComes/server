package com.pet.comes.repository;

import com.pet.comes.model.Entity.Alarm;
import com.pet.comes.model.Entity.Diary;
import com.pet.comes.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long> {

// 에러원인
//    Optional<Alarm> findByUserAAndTypeAndContentId(User user, Long diaryId, int type);

    @Query("select a from Alarm a where a.user = :user and a.diary= :diary and a.type= :type")
    List<Alarm> findAllByUserAndTypeAndContendId(@Param("user") User user, @Param("diary") Diary diary, @Param("type") int type);

    @Query("select a from Alarm a where a.user = :user and a.isChecked= :isChecked")
    List<Alarm> findAllByUserAndNotChecked(@Param("user") User user,@Param("isChecked")int isChecked);

    List<Alarm> findAllByUser(User user);
}
