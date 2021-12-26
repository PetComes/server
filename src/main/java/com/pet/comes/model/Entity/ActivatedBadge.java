package com.pet.comes.model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // Auditing : 감시, 자동으로 시간을 매핑하여 DB 테이블에 넣어줌.
@Table(name = "activated_badge") // DB테이블과 이름 다를 때
public class ActivatedBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : 데이터베이스에 위임( MYSQL )
    private Long userId;

    private String badgeId;

    private String status; // 상태 예시 알려주세요 ~

    @LastModifiedDate
    private LocalDateTime activatedAt; // Time

}
