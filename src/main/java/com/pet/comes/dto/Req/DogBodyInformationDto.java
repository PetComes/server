package com.pet.comes.dto.Req;

import lombok.Data;

@Data
public class DogBodyInformationDto {
    private long dogId;
    private long modifiedBy;
    private float height;
    private float weight;
}
