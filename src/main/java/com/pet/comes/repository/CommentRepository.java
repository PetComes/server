package com.pet.comes.repository;

import com.pet.comes.model.Entity.Comment;
import com.pet.comes.model.Entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {//<Comment, CommentID> {

    //     @Query("select  User.nickname,User.imageUrl,Comment.text,Comment.commentedAt,Comment .commentCommentId from Comment join User on Comment.userId = User.id")
    // 대소 문자 구별
    @Query("select u.nickname, u.imageUrl, c.text, c.commentedAt, c.commentCommentId from Comment c left join User u on c.user.id = u.id " +
            "where c.diaryId= :diaryId")
    // nativeQuery 옵션을 줘야 DB에ㄹ서 쿼리문을 작성하는 방식으로 작성할 수 있음.
    List<Object[]> findAllByDiaryIdForD6(@Param("diaryId") Long diaryId);

    @Query("select u.nickname, u.imageUrl from Comment c left join User u " +
            "where c.diaryId= :diaryId")
    List<Object[]> findAllByDiaryIdForH3(@Param("diaryId") Long diaryId);

}
