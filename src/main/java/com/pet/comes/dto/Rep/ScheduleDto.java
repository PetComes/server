package com.pet.comes.dto.Rep;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ScheduleDto {
	private final Long id;
	private final Long userId;
	private final Long dogId;
	private final LocalDate date;
	private final LocalTime time;
	private final String memo;
	private final Class dType;
}
