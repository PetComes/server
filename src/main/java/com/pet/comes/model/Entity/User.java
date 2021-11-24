package com.pet.comes.model.Entity;

import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.model.EnumType.SocialType;
import com.pet.comes.model.EnumType.UserStatus;
import com.pet.comes.model.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private final UserStatus status = UserStatus.NORMAL;

    private String name;
    private String email;
    private String password;
    private String nickname;
    private String introduction;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private String code;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType = SocialType.NOT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "family_id")
    private Family family;        //단방향 연관관계

    @OneToMany(mappedBy = "user")
    private List<Diary> diaries = new ArrayList<Diary>();

    public User(UserJoinDto userJoinDto) {
        this.name = userJoinDto.getName();
        this.email = userJoinDto.getEmail();
        this.password = userJoinDto.getPassword();
        this.nickname = userJoinDto.getNickname();
        this.introduction = userJoinDto.getIntroduction();
        this.imageUrl = userJoinDto.getImageUrl();
    }

    public void setFamilyId(Family family) {
        if (this.family == null) {
            this.family = family;
        }
    }

    public void setDiaries(Diary diary){
        this.diaries.add(diary);

        if(diary.getUser() != this)
            diary.setUser(this);
    }
}
