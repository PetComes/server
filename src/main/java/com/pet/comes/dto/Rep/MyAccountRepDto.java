package com.pet.comes.dto.Rep;

import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.EnumType.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public class MyAccountRepDto {
    private UserStatus status;
    private String name;
    private String email;
    private String password;
    private String nickname;
    private String introduction;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<String> dogsName;

    public MyAccountRepDto(User user) {
        this.status = user.getStatus();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
        this.introduction = user.getIntroduction();
        this.imageUrl = user.getImageUrl();
        this.createdAt = user.getCreatedAt();
        this.modifiedAt = user.getModifiedAt();

        Family family = user.getFamily();
        List<String> tmp = new ArrayList<String>();

        if (family == null) {
            tmp.add("반려견을 추가해보세요 !");
            this.dogsName = tmp;
        } else {
            List<Dog> dogs = family.getDogs();
            for (int i = 0; i < dogs.size(); i++) {
                tmp.add(dogs.get(i).getName());
            }
            this.dogsName = tmp;
        }
    }
}
