package com.pet.comes.model.Entity;

import com.pet.comes.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Schedule extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column( columnDefinition = "TINYINT", length=1)
    private int doesHaveAdditions; // DEFAULT 0 COMMENT '추가 항목 있음=1 / 없음 = 0'

    private int iconId;

    @Column(columnDefinition = "TINYINT", length = 1)
    private int alsoMonthly; // '월간에 노출하기=1 , 일간에서만 보기=0',





}
