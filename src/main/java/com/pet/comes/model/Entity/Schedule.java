package com.pet.comes.model.Entity;

import com.pet.comes.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "schedule")
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.

public class Schedule  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column( columnDefinition = "TINYINT", length=1)
    private int doesHaveAdditions; // DEFAULT 0 COMMENT '추가 항목 있음=1 / 없음 = 0'

    private int iconId;

    @CreatedDate
    private LocalDateTime time ;

    @Column(columnDefinition = "TINYINT", length = 1)
    private int alsoMonthly; // '월간에 노출하기=1 , 일간에서만 보기=0',

}
