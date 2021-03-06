package com.pet.comes.dto.Req;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleReqDto {
	private int iconId;
	private long scheduleId;
	private long userId;
	private String date;
	private String time;
	private String memo;

	private String kind;
	private String type;
	private String amount;

	private String prescriptionUrl;
	private int expenses;

	private String disease;
	private long dogId;
	private double weight;
	private String address;
	private String x;
	private String y;
	private String locationName;

	private String startTime;
	private String endTime;

	private int additionalItemNo;
	private Map<String, String> etcMap;

}
