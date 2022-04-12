package com.pet.comes.model.Entity.schedule;

import javax.persistence.Column;
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
@DiscriminatorValue("DRUG")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Drug extends Schedule {

	private String kind;

	@Column(columnDefinition = "text")
	private String prescriptionUrl;

	private Integer expenses;

	/** 생성자 */
	public Drug(User user, Dog dog, ScheduleDto scheduleDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleDto.getDate());
		setTime(scheduleDto.getTime());
		setMemo(scheduleDto.getMemo());
		this.kind = scheduleDto.getKind();
		this.prescriptionUrl = scheduleDto.getPrescriptionUrl();
		this.expenses = scheduleDto.getExpenses();
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
