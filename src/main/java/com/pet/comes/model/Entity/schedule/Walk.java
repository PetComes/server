package com.pet.comes.model.Entity.schedule;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.dto.Req.ScheduleReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("WALK")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Walk extends Schedule {

	private LocalTime startTime;
	private LocalTime endTime;

	/** 생성자 */
	public Walk(User user, Dog dog, ScheduleReqDto scheduleReqDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleReqDto.getDate());
		setTime(scheduleReqDto.getTime());
		setMemo(scheduleReqDto.getMemo());
		this.startTime = LocalTime.parse(scheduleReqDto.getStartTime(), DateTimeFormatter.ISO_LOCAL_TIME);
		this.endTime = LocalTime.parse(scheduleReqDto.getEndTime(), DateTimeFormatter.ISO_LOCAL_TIME);
	}

	/** 수정자 */
	public void modifyStartTime(String startTime) {
		if(startTime != null) {
			this.startTime = LocalTime.parse(startTime, DateTimeFormatter.ISO_LOCAL_TIME);
		}
	}

	public void modifyEndTime(String endTime) {
		if(endTime != null) {
			this.endTime = LocalTime.parse(endTime, DateTimeFormatter.ISO_LOCAL_TIME);
		}
	}
}
