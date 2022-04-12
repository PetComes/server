package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("PLAY")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Play extends Schedule {

	/** 생성자 */
	public Play(User user, Dog dog, ScheduleDto scheduleDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleDto.getDate());
		setTime(scheduleDto.getTime());
		setMemo(scheduleDto.getMemo());
	}
}
