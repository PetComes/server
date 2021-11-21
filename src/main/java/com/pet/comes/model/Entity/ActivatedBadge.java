package com.pet.comes.model.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "activated_badge") // DB테이블과 이름 다를 때
public class ActivatedBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY : 데이터베이스에 위임( MYSQL )
    private Long userId;

    private String badgeId;

    private String date; // Time

    private String status; // 상태 예시 알려주세요 ~


}
