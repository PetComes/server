package com.pet.comes.model.Entity;


import com.pet.comes.dto.Req.DiaryReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "diary")
public class Diary {//extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long dogId;

    @Column(columnDefinition = "TEXT") //
    private String text;

    private int howManyDogs;


    @Column(name = "image_url",columnDefinition = "TEXT")
    private String diaryImgUrl;

    @OneToOne(fetch = FetchType.LAZY)// default : EAGER
    @JoinColumn(name = "address_id") // 연관관계 주인 : 주 테이블에 연관관계 주인 있는 것을 선호함.
    private Address address;

    @Column(name = "is_public",nullable = false, columnDefinition = "TINYINT", length = 1) // DBMS의 테이블과 매핑시 오류방지
    private int isPublic; // 1: 공개 , 0 : 비공개

    @Column(name = "how_many_comments")
    private int howManyComments; // 다이어리에 달린 댓글 수

    @Column(name = "how_many_pins")
    private int howManyPins; // 다이어리가 pin된 갯수

    @CreatedDate
    private LocalDateTime registeredAt;

    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1) // DBMS의 테이블과 매핑시 오류방지
    private int isDeleted; // 1: 삭제 , 0 : 정상

    public Diary(@RequestBody DiaryReqDto diaryReqDto) {
        this.dogId = diaryReqDto.getDogId();
        this.text = diaryReqDto.getText();
        this.howManyDogs = diaryReqDto.getHowManyDogs();
        this.isPublic = diaryReqDto.getIsPublic();
        this.isDeleted = diaryReqDto.getIsDeleted();
        this.diaryImgUrl = diaryReqDto.getImageUrl();
    }

    public void modify(DiaryReqDto diaryReqDto) {
        this.howManyDogs = diaryReqDto.getHowManyDogs();
        this.isPublic = diaryReqDto.getIsPublic();
        this.text = diaryReqDto.getText();
        this.isDeleted = diaryReqDto.getIsDeleted();
        this.diaryImgUrl = diaryReqDto.getImageUrl();
    }

    public void toggle(int isPublic) {
        this.isPublic = isPublic;
    }

    public void setUser(User user) {
        if (this.user != null) {
            this.user.getDiaries().remove(this);
        }
        this.user = user;
        if (!user.getDiaries().contains(this))
            user.setDiaries(this);
    }

    public void setAddress(Address address) {
        if (this.address == null)
            this.address = address;
    }
}
