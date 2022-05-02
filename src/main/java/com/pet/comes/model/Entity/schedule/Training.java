package com.pet.comes.model.Entity.schedule;

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
@DiscriminatorValue("TRAINING")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Training extends Schedule {

	/** 생성자 */
	public Training(User user, Dog dog, ScheduleReqDto scheduleReqDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleReqDto.getDate());
		setTime(scheduleReqDto.getTime());
		setMemo(scheduleReqDto.getMemo());
	}
}
