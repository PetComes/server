package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.model.Entity.Address;

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

}
