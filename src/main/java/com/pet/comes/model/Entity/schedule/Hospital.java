package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("HOSPITAL")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Hospital extends Schedule {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;

	private String disease;

	private String kind;

	private String prescriptionUrl;

	private Integer expenses;

	private Double weight;

	/** 생성자 */
	public Hospital(User user, ScheduleDto scheduleDto) {
		setUser(user);
		setDate(scheduleDto.getDate());
		setTime(scheduleDto.getTime());
		setMemo(scheduleDto.getMemo());
		this.disease = scheduleDto.getDisease();
		this.kind = scheduleDto.getKind();
		this.prescriptionUrl = scheduleDto.getPrescriptionUrl();
		this.expenses = scheduleDto.getExpenses();
		this.weight = scheduleDto.getWeight();
	}
}
