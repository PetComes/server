package com.pet.comes.model.Entity;


import com.pet.comes.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Time;

@Entity
@Getter
@NoArgsConstructor
@IdClass(Family.class) // PK 2개일때
public class Family extends Timestamped implements Serializable { // @Idclass 있을 시 Serializable 인터페이스 구현

    @Id
    private Long userId;

    @Id
    private Long familyId;

    private int isUser; // 가족 id 가 유저이면 1, 강아지면 0




}
