package com.pet.comes.model.Entity;

import com.pet.comes.model.Timestamped;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "alarm")
@NoArgsConstructor
@Entity
public class Alarm extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long addressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1) // DBMS의 테이블과 매핑시 오류방지
    private int type; // 1: 댓글, 0: 핀

    @Column(nullable = false, columnDefinition = "TINYINT", length = 1) // DBMS의 테이블과 매핑시 오류방지
    private int isChecked; // 1: 읽음, 0: 읽지 않음.

    private Long contentId;

    public Alarm(User user, int type, int isChecked,Long contentId) {
        this.user = user;
        this.type = type;
        this.isChecked = isChecked;
        this.contentId = contentId;
    }


}
