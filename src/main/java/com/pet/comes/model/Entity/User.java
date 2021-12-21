package com.pet.comes.model.Entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.comes.dto.Join.UserJoinDto;
import com.pet.comes.dto.Req.SignInReqDto;
import com.pet.comes.model.EnumType.SocialType;
import com.pet.comes.model.EnumType.UserStatus;
import com.pet.comes.model.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User extends Timestamped implements UserDetails {

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

    @OneToMany(mappedBy = "user")
    private List<Pin> pins = new ArrayList<>();

    private String roles;

    private String refreshToken;

    public User(UserJoinDto userJoinDto) {
        this.name = userJoinDto.getName();
        this.email = userJoinDto.getEmail();
        this.password = userJoinDto.getPassword();
        this.nickname = userJoinDto.getNickname();
        this.introduction = userJoinDto.getIntroduction();
        this.imageUrl = userJoinDto.getImageUrl();
    }

    public User(SignInReqDto signReqDto) {
        this.email = signReqDto.getEmail();
        this.password = signReqDto.getPassword();

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

    public void setPins(Pin pin){
        this.pins.add(pin);

        if(pin.getUser() !=this)
            pin.setUser(this);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(roles));
        return auth;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
