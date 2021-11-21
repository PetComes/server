package com.pet.comes.model.Entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private int push; // 푸시 알림 설정 ( 1 : 활성화 , 2: 비활성화 )

    private int dead; // 반려견 보내주기 설정 ( 1: 활성화 , 0 : 비활성화 )


}
