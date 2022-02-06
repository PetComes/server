package com.pet.comes.model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "activated_badge") // DB테이블과 이름 다를 때
public class ActivatedBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // private String badgeId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "badge_id")
    private Badge badge;

    private String status; // 상태 예시 알려주세요 ~ : 활성화 상태일 때(NORMAL), 등록취소되었을 때(CANCELED)

    @LastModifiedDate
    private LocalDateTime activatedAt; // Time

}
