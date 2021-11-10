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
    private Long userId;

    @Id
    private long diaryId;
    
    private String pinedAt;


}
