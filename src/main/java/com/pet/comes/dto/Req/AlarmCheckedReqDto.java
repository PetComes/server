package com.pet.comes.dto.Req;


import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmCheckedReqDto {

    private Long userId;
    private int isChecked;

}
