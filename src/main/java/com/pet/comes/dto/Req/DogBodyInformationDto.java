package com.pet.comes.dto.Req;

import lombok.Data;

@Data
public class DogBodyInformationDto {
    private long dogId;
    private float weight;

    private long modifiedBy;
}
