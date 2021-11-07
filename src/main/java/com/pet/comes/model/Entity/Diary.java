package com.pet.comes.model.Entity;


import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class Diary extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT") //
    private String Text;

    private int howManyDogs;

    private Long addressId;

    @Column(nullable = false, columnDefinition = "TINYINT", length=1) // DBMS의 테이블과 매핑시 오류방지
    private int isPublic; // 1: 공개 , 0 : 비공개

    @Column(nullable = false, columnDefinition = "TINYINT", length=1) // DBMS의 테이블과 매핑시 오류방지
    private int isDeleted; // 1: 삭제 , 0 : 정상

    public Diary(@RequestBody DiaryReqDto diaryReqDto){
        this.userId = diaryReqDto.getUserId();
        this.Text = diaryReqDto.getText();
        this.howManyDogs = diaryReqDto.getHowManyDogs();
        this.addressId = diaryReqDto.getAddressId();
        this.isPublic = diaryReqDto.getIsPublic();
        this.isDeleted = diaryReqDto.getIsDeleted();
    }

}
