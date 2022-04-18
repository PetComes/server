package com.pet.comes.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pet.comes.dto.Rep.ScheduleDto;
import com.pet.comes.dto.Req.ScheduleConditionDto;
import com.pet.comes.dto.Req.ScheduleReqDto;
import com.pet.comes.model.Entity.schedule.Schedule;
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

	@GetMapping("/schedule")
	public ResponseEntity<DataResponse> getSchedules(@RequestBody ScheduleConditionDto scheduleConditionDto) {
		List<ScheduleDto> schedules = scheduleService.getSchedules(scheduleConditionDto);
		return new ResponseEntity<>(
			DataResponse.response(Status.SUCCESS, ResponseMessage.SUCCESS_REGISTER_SCHEDULE, schedules),
			HttpStatus.OK);
	}

	@PostMapping("/schedule")
	public ResponseEntity<DataResponse> registerSchedule(@RequestBody ScheduleReqDto scheduleReqDto) {
		checkDateAndTime(scheduleReqDto);
		long scheduleId = scheduleService.registerSchedule(scheduleReqDto);
		return new ResponseEntity<>(
			DataResponse.response(Status.SUCCESS, ResponseMessage.SUCCESS_REGISTER_SCHEDULE, scheduleId),
			HttpStatus.OK);
	}

	@PatchMapping("/schedule")
	public ResponseEntity<NoDataResponse> modifySchedule(@RequestBody ScheduleReqDto scheduleReqDto) {
		scheduleService.modifySchedule(scheduleReqDto);
		return new ResponseEntity<>(
			NoDataResponse.response(Status.SUCCESS, ResponseMessage.SUCCESS_MODIFY_SCHEDULE), HttpStatus.OK
		);
	}

	@DeleteMapping("/schedule")
	public ResponseEntity<NoDataResponse> deleteSchedule(@RequestBody ScheduleReqDto scheduleReqDto) {
		scheduleService.deleteSchedule(scheduleReqDto);
		return new ResponseEntity<>(
			NoDataResponse.response(Status.SUCCESS, ResponseMessage.SUCCESS_DELETE_SCHEDULE), HttpStatus.OK
		);
	}

	public void checkDateAndTime(ScheduleReqDto scheduleReqDto) {
		if (scheduleReqDto.getDate() == null) {
			scheduleReqDto.setDate(String.valueOf(LocalDate.now()));
		}
		if (scheduleReqDto.getTime() == null) {
			scheduleReqDto.setTime(makeCurrentTime(LocalTime.now()));
		}
	}

	public String makeCurrentTime(LocalTime now) {
		String currentTime = String.valueOf(now);
		return currentTime.substring(0, 6) + "00";
	}
}
