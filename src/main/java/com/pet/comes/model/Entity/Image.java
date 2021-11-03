package com.pet.comes.model.Entity;

import com.pet.comes.dto.Req.ImageReqDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long diaryId;

    @Column(columnDefinition = "TEXT",nullable = false) //
    private String url;

    public Image(@RequestBody ImageReqDto imageReqDto){
        this.diaryId = imageReqDto.getDiaryId();
        this.url = imageReqDto.getUrl();
    }


}
