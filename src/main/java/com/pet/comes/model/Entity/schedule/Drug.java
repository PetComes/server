package com.pet.comes.model.Entity.schedule;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.model.Entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("DRUG")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Drug extends Schedule {

	private String kind;

	@Column(columnDefinition = "text")
	private String prescriptionUrl;

	private Integer expenses;

	/** 생성자 */
	public Drug(User user, ScheduleDto scheduleDto) {
		setUser(user);
		setDate(scheduleDto.getDate());
		setTime(scheduleDto.getTime());
		setMemo(scheduleDto.getMemo());
		this.kind = scheduleDto.getKind();
		this.prescriptionUrl = scheduleDto.getPrescriptionUrl();
		this.expenses = scheduleDto.getExpenses();
	}
}
