package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.model.EnumType.PottyType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("POTTY")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Potty extends Schedule{

	@Enumerated(EnumType.STRING)
	private PottyType kind;
}
