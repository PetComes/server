package com.pet.comes.repository;

import com.pet.comes.model.Entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

    List<Diary> findAllByDogId(Long dogId);

    List<Diary> findAllByIsPublicOrderByIdDesc(int isPublic);

    List<Diary> findAllByOrderByIdDesc();

    @Query("select d.id as id, d.text as text, d.diaryImgUrl as diaryImageUrl , d.isPublic as isPublic, d.howManyComments as howManyComments, d.howManyPins as howManyPins, " +
            "u.imageUrl as userImageUrl, u.nickname as nickName, u.id as userId " +
            "from Diary d left join User u on d.user = u " +
            "where d.isPublic= 1 " +
            "order by d.id desc")
    List<IDiaryUserDto> findAllByIsPublicOrderByIdDescQuery();

    @Query("select d.id as id, d.text as text, d.diaryImgUrl as diaryImageUrl , d.isPublic as isPublic, d.howManyComments as howManyComments, d.howManyPins as howManyPins, " +
            "u.imageUrl as userImageUrl, u.nickname as nickName, u.id as userId " +
            "from Diary d left join User u on d.user = u " +
            "where d.isPublic= 1 " +
            "order by d.howManyPins desc")
    List<IDiaryUserDto> findAllByIsPublicOrderByHowManyPinsDescQuery();


//    List<Diary> findAllByIsPublicOrderByIdDesc( Pageable pageable); // 페이징 처리
//    @Query("")}

}
