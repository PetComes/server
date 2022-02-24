package com.pet.comes.model.Entity;

import com.pet.comes.model.EnumType.BadgeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
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

    private BadgeStatus status = BadgeStatus.ACTIVATED;

    @LastModifiedDate
    private LocalDateTime activatedAt;

    public ActivatedBadge(Long userId, Badge badge, BadgeStatus status, LocalDateTime activatedAt) {
        this.userId = userId;
        this.badge = badge;
        this.status = status;
        this.activatedAt = activatedAt;
    }
}
