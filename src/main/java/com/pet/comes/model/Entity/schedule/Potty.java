package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.dto.Req.ScheduleReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.EnumType.PottyType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("POTTY")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Potty extends Schedule {

	@Enumerated(EnumType.STRING)
	private PottyType kind;

	/** 생성자 */
	public Potty(User user, Dog dog, ScheduleReqDto scheduleReqDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleReqDto.getDate());
		setTime(scheduleReqDto.getTime());
		setMemo(scheduleReqDto.getMemo());
		this.kind = PottyType.getValidatedEnumValue(scheduleReqDto.getKind());
	}

	/** 수정자 */
	public void modifyKind(String kind) {
		if(kind != null) {
			this.kind = PottyType.getValidatedEnumValue(kind);
		}
	}
}
