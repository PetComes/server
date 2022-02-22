package com.pet.comes.dto.Rep;


import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class AddDogsRepDto {

    private List<DogsProfileRepDto> getDogsProfileRepDtoList; // dogName, dogImageUrl;


}
