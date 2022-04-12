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
@DiscriminatorValue("SALON")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Salon extends Schedule {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "address_id")
	private Address address;

	private Integer expenses;

	/** 생성자 */
	public Salon(User user, Dog dog, Address address, ScheduleDto scheduleDto) {
		setUser(user);
		setDog(dog);
		setDate(scheduleDto.getDate());
		setTime(scheduleDto.getTime());
		setMemo(scheduleDto.getMemo());
		this.address = address;
		this.expenses = scheduleDto.getExpenses();
	}
}
