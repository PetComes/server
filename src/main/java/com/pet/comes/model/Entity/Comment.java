package com.pet.comes.model.Entity;


import com.pet.comes.dto.Req.CommentReqDto;
import com.pet.comes.model.IdClass.CommentID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@IdClass(CommentID.class) // 식별자 클래스를 매핑
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "Comment")
public class Comment  implements Serializable {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "diary_id")
    private Long diaryId;

    @CreatedDate
    private LocalDateTime commentedAt;

    @Column(columnDefinition = "TEXT")
    private String text;

    public Comment(CommentReqDto commentReqDto){
        this.userId = commentReqDto.getUserId();
        this.diaryId = commentReqDto.getDiaryId();
        this.text = commentReqDto.getText();
    }
}
