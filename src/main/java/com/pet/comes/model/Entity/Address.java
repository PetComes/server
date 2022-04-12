package com.pet.comes.model.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.pet.comes.dto.Req.DiaryReqDto;
import com.pet.comes.dto.Req.ScheduleDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Address(ScheduleDto scheduleDto) {
        this.address = scheduleDto.getAddress();
        this.locationName = scheduleDto.getLocationName();
        this.x = scheduleDto.getX();
        this.y = scheduleDto.getY();
    }
}
