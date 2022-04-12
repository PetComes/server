package com.pet.comes.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.response.DataResponse;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.service.ScheduleService;
import com.pet.comes.response.Status;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ScheduleController {

	private final ScheduleService scheduleService;
	private static final NoDataResponse success = new NoDataResponse(Status.SUCCESS, ResponseMessage.SUCCESS);

	@PostMapping("/schedule")
	public ResponseEntity<DataResponse> registerSchedule(@RequestBody ScheduleDto scheduleDto) {
		checkDateAndTime(scheduleDto);
		long scheduleId = scheduleService.registerSchedule(scheduleDto);
		if (scheduleId == 0) {
			return new ResponseEntity<>(
				DataResponse.response(Status.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR, scheduleId),
				HttpStatus.OK);
		}
		return new ResponseEntity<>(DataResponse.response(Status.SUCCESS, ResponseMessage.SUCCESS, scheduleId),
			HttpStatus.OK);
	}

	public void checkDateAndTime(ScheduleDto scheduleDto) {
		if(scheduleDto.getDate() == null) {
			scheduleDto.setDate(String.valueOf(LocalDate.now()));
		}
		if(scheduleDto.getTime() == null) {
			scheduleDto.setTime(makeCurrentTime(LocalTime.now()));
		}
	}

	public String makeCurrentTime(LocalTime now) {
		String currentTime = String.valueOf(now);
		return currentTime.substring(0,6) + "00";
	}
}
