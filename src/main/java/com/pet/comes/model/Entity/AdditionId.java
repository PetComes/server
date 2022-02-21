package com.pet.comes.model.Entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Embeddable
public class AdditionId implements Serializable {

	@ManyToOne
	@JoinColumn(referencedColumnName = "id", name = "schedule_id")
	private Schedule schedule;
}
