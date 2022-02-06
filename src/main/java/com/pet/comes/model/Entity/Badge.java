package com.pet.comes.model.Entity;


import com.pet.comes.model.Timestamped;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "badge")
public class Badge extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String activeCondition; // 활성 조건 설명

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @OneToMany(mappedBy = "badge")
    private List<ActivatedBadge> activatedBadgeList;

}
