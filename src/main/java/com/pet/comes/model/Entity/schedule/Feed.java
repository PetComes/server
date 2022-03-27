package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;

import com.pet.comes.model.EnumType.FeedType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("FEED")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Feed extends Schedule {

	private String kind;

	@Enumerated(EnumType.STRING)
	private FeedType type;

	private String amount;
}
