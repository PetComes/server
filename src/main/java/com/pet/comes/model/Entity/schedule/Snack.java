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
@DiscriminatorValue("SNACK")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Snack extends Schedule {

	private String kind;

	/** 생성자 */
	public Snack(User user, Dog dog, ScheduleReqDto scheduleReqDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleReqDto.getDate());
		setTime(scheduleReqDto.getTime());
		setMemo(scheduleReqDto.getMemo());
		this.kind = scheduleReqDto.getKind();
	}

	/** 수정자 */
	public void modifyKind(String kind) {
		if(kind != null) {
			this.kind = kind;
		}
	}
}
