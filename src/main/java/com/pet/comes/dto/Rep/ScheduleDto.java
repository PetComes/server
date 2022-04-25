package com.pet.comes.dto.Rep;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDto {
	private Long id;
	private Long userId;
	private Long dogId;
	private LocalDate date;
	private LocalTime time;
	private String memo;
	private String dType;

	public ScheduleDto(Long id, Long userId, Long dogId, LocalDate date, LocalTime time, String memo, Class dType) {
		this.id = id;
		this.userId = userId;
		this.dogId = dogId;
		this.date = date;
		this.time = time;
		this.memo = memo;

		if(dType.getSimpleName().equals("Schedule")) {
			this.dType = "Etc";
		}
		else {
			this.dType = dType.getSimpleName();
		}
	}
}
