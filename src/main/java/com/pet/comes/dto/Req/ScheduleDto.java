package com.pet.comes.dto.Req;

import lombok.Getter;

@Getter
public class ScheduleDto {
	private Long iconId;
	private Long scheduleId;
	private Long userId;

	public ScheduleDto(long iconId, long scheduleId, long userId) {
		this.iconId = iconId;
		this.scheduleId = scheduleId;
		this.userId = userId;
	}
}
