package com.pet.comes.model.Entity;


import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "with_dogs") // DB테이블과 이름 다를 때
public class WithDogs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long diaryId;

    private Long dogId;

}
