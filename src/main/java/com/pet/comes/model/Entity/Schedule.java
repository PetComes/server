package com.pet.comes.model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.pet.comes.model.schedule.CommonItems;

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
    private LocalDateTime scheduledAt;

    @Column(columnDefinition = "TINYINT", length = 1)
    private int showOnMonthly; // '월간에 노출하기=1 , 일간에서만 보기=0'

    @OneToMany(mappedBy = "scheduleId")
    private List<Addition> additionList;

    public void addAddition(Addition addition) {
        this.additionList.add(addition);
        if(addition.getAdditionId().getSchedule() != this) {
            addition.setSchedule(this);
        }
    }

    public Schedule(Map<String, String> scheduleParameters) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        LocalDateTime scheduledAt = LocalDateTime.parse(scheduleParameters.get("scheduledAt"), formatter);

        this.iconId = Integer.parseInt(scheduleParameters.get("iconId"));
        this.scheduledAt = scheduledAt;
        this.notes = scheduleParameters.get("notes");
        this.userId = Long.parseLong(scheduleParameters.get("userId"));
        this.showOnMonthly = Integer.parseInt(scheduleParameters.get("showOnMonthly"));
        this.doesHaveAdditions = Integer.parseInt(scheduleParameters.get("doesHaveAdditions"));
    }

}
