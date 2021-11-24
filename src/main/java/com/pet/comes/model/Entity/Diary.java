package com.pet.comes.model.Entity;


import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "diary")
public class Diary {//extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private Long dogId;

    @Column(columnDefinition = "TEXT") //
    private String Text;

    private int howManyDogs;


    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private Long addressId;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1) // DBMS의 테이블과 매핑시 오류방지
    private int isPublic; // 1: 공개 , 0 : 비공개

    @CreatedDate
    private LocalDateTime registeredAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1) // DBMS의 테이블과 매핑시 오류방지
    private int isDeleted; // 1: 삭제 , 0 : 정상

    public Diary(@RequestBody DiaryReqDto diaryReqDto) {
        this.dogId = diaryReqDto.getDogId();
        this.Text = diaryReqDto.getText();
        this.howManyDogs = diaryReqDto.getHowManyDogs();
        this.addressId = diaryReqDto.getAddressId();
        this.isPublic = diaryReqDto.getIsPublic();
        this.isDeleted = diaryReqDto.getIsDeleted();
        this.imageUrl = diaryReqDto.getImageUrl();
    }

    public void modify(DiaryReqDto diaryReqDto) {
        this.howManyDogs = diaryReqDto.getHowManyDogs();
        this.isPublic = diaryReqDto.getIsPublic();
        this.Text = diaryReqDto.getText();
        this.addressId = diaryReqDto.getAddressId();
        this.isDeleted = diaryReqDto.getIsDeleted();
        this.imageUrl = diaryReqDto.getImageUrl();
    }

    public void toggle(int isPublic) {
        this.isPublic = isPublic;
    }

    public void setUser(User user){
        if(this.user !=null){
            this.user.getDiaries().remove(this);
        }
        this.user =user;
        if(!user.getDiaries().contains(this))
            user.setDiaries(this);
    }
}
