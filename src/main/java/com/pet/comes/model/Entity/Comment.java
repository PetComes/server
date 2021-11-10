package com.pet.comes.model.Entity;


import com.pet.comes.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@IdClass(Comment.class)
public class Comment  implements Serializable {

    @Id
    private Long userId;

    @Id
    private Long diaryId;

    private LocalDateTime commentedAt;

    @Column(columnDefinition = "TEXT")
    private String text;

}
