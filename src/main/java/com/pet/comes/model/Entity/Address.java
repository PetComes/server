package com.pet.comes.model.Entity;


import com.pet.comes.dto.Req.DiaryReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String locationName;

    @Column(columnDefinition = "TEXT")
    private String x;

    @Column(columnDefinition = "TEXT")
    private String y;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @OneToOne(mappedBy = "address")
    private Diary diary;

    public Address(DiaryReqDto diaryReqDto) {
        this.address = diaryReqDto.getAddress();
        this.locationName = diaryReqDto.getLocationName();
        this.x = diaryReqDto.getX();
        this.y = diaryReqDto.getY();

    }

    public void setDiary(Diary diary) {
        if (this.diary == null)
            this.diary = diary;
    }
}
