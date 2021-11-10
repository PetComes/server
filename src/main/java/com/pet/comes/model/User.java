package com.pet.comes.model;

import com.pet.comes.dto.UserJoinDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(value = EnumType.STRING)
    private UserStatus status = UserStatus.NORMAL;

    private String name;
    private String email;
    private String password;
    private String nickname;
    private String introduction;
    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private SocialType socialType = SocialType.NOT;

    private String code;

    @ManyToOne
    @JoinColumn(name="family_id")
    private Family family;        //단방향 연관관계


    public User(UserJoinDto userJoinDto) {
        this.name = userJoinDto.getName();
        this.email = userJoinDto.getEmail();
        this.password = userJoinDto.getPassword();
        this.nickname = userJoinDto.getNickname();
        this.introduction = userJoinDto.getIntroduction();
        this.imageUrl = userJoinDto.getImageUrl();
    }
}
