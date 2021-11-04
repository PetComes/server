package com.pet.comes.model.Entity;


import com.pet.comes.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@NoArgsConstructor
@IdClass(Pin.class)
public class Pin extends Timestamped implements Serializable  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long diaryId;
    
    private String pinedAt;


}
