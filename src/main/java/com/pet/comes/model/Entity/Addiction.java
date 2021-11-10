package com.pet.comes.model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Addiction {

    @Id
    private Long scheduleId; // 일정 id

    private String title;  // 추가항목의 이름 ex) 위치

    @Column(columnDefinition = "TEXT")
    private String contents;

}
