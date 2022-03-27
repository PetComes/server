package com.pet.comes.model.Entity.schedule;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

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
}
