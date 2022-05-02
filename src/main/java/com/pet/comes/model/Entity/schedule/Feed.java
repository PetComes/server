package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.dto.Req.ScheduleReqDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.EnumType.FeedType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("FEED")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Feed extends Schedule {

	private String kind;

	@Enumerated(EnumType.STRING)
	private FeedType type;

	private String amount;

	/** 생성자 */
	public Feed(User user, Dog dog, ScheduleReqDto scheduleReqDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleReqDto.getDate());
		setTime(scheduleReqDto.getTime());
		setMemo(scheduleReqDto.getMemo());
		this.kind = scheduleReqDto.getKind();
		this.type = FeedType.getValidatedEnumValue(scheduleReqDto.getType());
		this.amount = scheduleReqDto.getAmount();
	}

	/** 수정자 */
	public void modifyKind(String kind) {
		if(kind != null) {
			this.kind = kind;
		}
	}

	public void modifyType(String type) {
		if(type != null) {
			this.type = FeedType.getValidatedEnumValue(type);
		}
	}

	public void modifyAmount(String amount) {
		if(amount != null) {
			this.amount = amount;
		}
	}
}
