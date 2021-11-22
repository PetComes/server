package com.pet.comes.repository;

import com.pet.comes.dto.Rep.CommentListRepDto;
import com.pet.comes.model.Entity.Comment;
import com.pet.comes.model.Entity.Diary;
import com.pet.comes.model.IdClass.CommentID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {//<Comment, CommentID> {

//     @Query("select  User.nickname,User.imageUrl,Comment.text,Comment.commentedAt,Comment .commentCommentId from Comment join User on Comment.userId = User.id")
    @Query("select u.nickname, u.imageUrl, c.text, c.commentedAt, c.commentCommentId from Comment c left join User u on c.user.id = u.id " +
            "where c.diaryId= :diaryId")
    // nativeQuery 옵션을 줘야 DB에서 쿼리문을 작성하는 방식으로 작성할 수 있음.
    List<Object[]> findAllByDiaryId(@Param("diaryId") Long diaryId);

}
