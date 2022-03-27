package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.model.Entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("SNACK")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Snack extends Schedule {

	private String kind;

	/** 생성자 */
	public Snack(User user, ScheduleDto scheduleDto) {
		setUser(user);
		setDate(scheduleDto.getDate());
		setTime(scheduleDto.getTime());
		setMemo(scheduleDto.getMemo());
		this.kind = scheduleDto.getKind();
	}
}
