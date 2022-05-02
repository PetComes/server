package com.pet.comes.dto.Req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleConditionDto {
	private long userId;
	private char flag;
	private String date;
}
