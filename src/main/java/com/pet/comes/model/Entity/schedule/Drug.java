package com.pet.comes.model.Entity.schedule;

import javax.persistence.Column;
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
@DiscriminatorValue("DRUG")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Drug extends Schedule {

	private String kind;

	@Column(columnDefinition = "text")
	private String prescriptionUrl;

	private Integer expenses;

	/** 생성자 */
	public Drug(User user, Dog dog, ScheduleReqDto scheduleReqDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleReqDto.getDate());
		setTime(scheduleReqDto.getTime());
		setMemo(scheduleReqDto.getMemo());
		this.kind = scheduleReqDto.getKind();
		this.prescriptionUrl = scheduleReqDto.getPrescriptionUrl();
		this.expenses = scheduleReqDto.getExpenses();
	}

	/** 생성자 */
	public void modifyKind(String kind) {
		if(kind != null) {
			this.kind = kind;
		}
	}

	public void modifyPrescriptionUrl(String url) {
		if(url != null) {
			this.prescriptionUrl = url;
		}
	}

	public void modifyExpenses(Integer expenses) {
		if(expenses != null) {
			this.expenses = expenses;
		}
	}
}
