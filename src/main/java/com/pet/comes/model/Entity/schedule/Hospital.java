package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.Dog;
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
	public Hospital(User user, Dog dog, Address address, ScheduleDto scheduleDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleDto.getDate());
		setTime(scheduleDto.getTime());
		setMemo(scheduleDto.getMemo());

		this.address = address;
		this.disease = scheduleDto.getDisease();
		this.kind = scheduleDto.getKind();
		this.prescriptionUrl = scheduleDto.getPrescriptionUrl();
		this.expenses = scheduleDto.getExpenses();

		if(scheduleDto.getWeight() != 0.0) {
			this.weight = scheduleDto.getWeight();
		}
	}

	/** 수정자 */
	public void modifyDisease(String disease) {
		if(disease != null) {
			this.disease = disease;
		}
	}

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

	public void modifyExpenses(int expenses) {
		if(expenses != 0) {
			this.expenses = expenses;
		}
	}

	public void modifyAddress(Address address) {
		if(address != null) {
			this.address = address;
		}
	}

	public void modifyWeight(double weight) {
		if(weight != 0.0) {
			this.weight = weight;
		}
	}
}
