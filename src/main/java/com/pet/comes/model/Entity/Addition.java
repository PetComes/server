package com.pet.comes.model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.pet.comes.model.schedule.CommonItems;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "addition")
public class Addition {

    // @Id
    // private Long scheduleId; // 일정 id

    @EmbeddedId
    private AdditionId additionId = new AdditionId();

    private String title;  // 추가항목의 이름 ex) 위치

    @Column(columnDefinition = "TEXT")
    private String contents;

    public void setSchedule(Schedule schedule) {
        if(!schedule.getAdditionList().contains(this)) {
            schedule.addAddition(this);
        }
    }

}
