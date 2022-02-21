package com.pet.comes.model.schedule;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Feeding extends CommonItems {
	private String kindOfFeed;
	private String amountOfFeed;
	private String dryOrWetFeed;

	public Feeding(Map<String, String> scheduleParameters) {
		this.setter(scheduleParameters);
		this.kindOfFeed = scheduleParameters.get("kindOfFeed");
		this.amountOfFeed = scheduleParameters.get("amountOfFeed");
		this.dryOrWetFeed = scheduleParameters.get("dryOrWetFeed");
	}

	@Override
	void setter(Map<String, String> scheduleParameters) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		LocalDateTime scheduledAt = LocalDateTime.parse(scheduleParameters.get("scheduledAt"), formatter);

		this.iconId = Integer.parseInt(scheduleParameters.get("iconId"));
		this.scheduledAt = scheduledAt;
		this.notes = scheduleParameters.get("notes");
		this.userId = Long.parseLong(scheduleParameters.get("userId"));
		this.showOnMonthly = Integer.parseInt(scheduleParameters.get("showOnMonthly"));
		this.doesHaveAdditions = Integer.parseInt(scheduleParameters.get("doesHaveAdditions"));
	}
}
