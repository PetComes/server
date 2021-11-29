package com.pet.comes.model.IdClass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;


@Data // equals, hashCode 구현
@NoArgsConstructor // 기본 생성자 필요함.
@AllArgsConstructor
public class PinID implements Serializable {


    private Long userId;
    private Long diaryId;
}
