package com.pet.comes.model.Entity;


import com.pet.comes.dto.Req.CommentReqDto;
import com.pet.comes.model.IdClass.CommentID;
import com.pet.comes.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor
//@IdClass(CommentID.class) // 식별자 클래스를 매핑
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "comment") //
public class Comment  { //implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long commentId;

    @OneToOne(fetch = FetchType.LAZY) //
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "diary_id")
    private Long diaryId;

    @CreatedDate
    private LocalDateTime commentedAt;

    @Column(columnDefinition = "TEXT")
    private String text;

    @Column(name = "comment_comment_id")
    private Long commentCommentId;



    public Comment(CommentReqDto commentReqDto,User user){
        this.user =user;
        this.diaryId = commentReqDto.getDiaryId();
        this.text = commentReqDto.getText();
        this.commentCommentId = commentReqDto.getCommentCommentId();
    }
}
