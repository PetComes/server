package com.pet.comes.dto.Req;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleListDto {

	private Long userId;
	private String date;

	public LocalDateTime dateToLocalDateTime() {
		return LocalDateTime.parse(date, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
}
