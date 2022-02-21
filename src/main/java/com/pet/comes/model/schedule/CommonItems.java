package com.pet.comes.model.schedule;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Getter;

@Getter
public abstract class CommonItems {
	int iconId;
	LocalDateTime scheduledAt;
	String notes;
	Long userId;
	int showOnMonthly;
	int doesHaveAdditions;

	abstract void setter(Map<String, String> scheduleParams);
}
