package com.pet.comes.repository;

import com.pet.comes.dto.Rep.IDiaryUserRepDto;
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


    //https://www.baeldung.com/jpa-queries-custom-result-with-aggregation-functions
    // ** as로 재정의 해줘야 projection 인터페이스에서 인식함.
    @Query("select d.id as id, d.text as text, a.locationName as locationName , d.diaryImgUrl as diaryImageUrl , d.isPublic as isPublic, d.howManyComments as howManyComments, d.howManyPins as howManyPins, d.registeredAt as registeredAt , " +
            "u.imageUrl as userImageUrl, u.nickname as nickName, u.id as userId , dog.name as dogName " +
            "from Diary d left join User u on d.user = u left outer join Address a on d.address = a " +
            "left join Dog dog on d.dogId = dog.id " +
            "where d.isPublic= 1 " +
            "order by d.id desc")
    List<IDiaryUserRepDto> findAllByIsPublicOrderByIdDescQuery();


    @Query("select d.id as id, d.text as text, a.locationName as locationName , d.diaryImgUrl as diaryImageUrl , d.isPublic as isPublic, d.howManyComments as howManyComments, d.howManyPins as howManyPins, d.registeredAt as registeredAt , " +
            "u.imageUrl as userImageUrl, u.nickname as nickName, u.id as userId , dog.name as dogName " +
            "from Diary d left join User u on d.user = u left outer join Address a on d.address = a " +
            "left join Dog dog on d.dogId = dog.id " +
            "where d.isPublic= 1 " +
            "order by d.howManyPins desc")
    List<IDiaryUserRepDto> findAllByIsPublicOrderByHowManyPinsDescQuery();


//    List<Diary> findAllByIsPublicOrderByIdDesc( Pageable pageable); // 페이징 처리
//    @Query("")}

}
