package com.pet.comes.dto.Rep;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.schedule.Bath;
import com.pet.comes.model.Entity.schedule.Drug;
import com.pet.comes.model.Entity.schedule.EtcItem;
import com.pet.comes.model.Entity.schedule.Feed;
import com.pet.comes.model.Entity.schedule.Hospital;
import com.pet.comes.model.Entity.schedule.Menstruation;
import com.pet.comes.model.Entity.schedule.Play;
import com.pet.comes.model.Entity.schedule.Potty;
import com.pet.comes.model.Entity.schedule.Salon;
import com.pet.comes.model.Entity.schedule.Schedule;
import com.pet.comes.model.Entity.schedule.Sleep;
import com.pet.comes.model.Entity.schedule.Snack;
import com.pet.comes.model.Entity.schedule.Training;
import com.pet.comes.model.Entity.schedule.Walk;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class DetailScheduleDto {
	private int iconId;
	private long scheduleId;
	private long userId;
	private long dogId;
	private String date;
	private String time;
	private String memo;

	private String kind;
	private String type;
	private String amount;

	private String prescriptionUrl;
	private int expenses;

	private String disease;
	private double weight;
	private String address;
	private String x;
	private String y;
	private String locationName;

	private String startTime;
	private String endTime;

	private List<EtcItemDto> etcItemDtoList;

	public static DetailScheduleDto convertToDetailScheduleDto(Schedule schedule) {
		DetailScheduleDto dto = new DetailScheduleDto();
		setBasicInformation(dto, schedule);

		if (schedule instanceof Feed) {
			Feed feed = (Feed)schedule;
			dto.setIconId(1);
			dto.setKind(feed.getKind());
			dto.setType(feed.getType().toString());
			dto.setAmount(feed.getAmount());
			return dto;
		}
		if (schedule instanceof Snack) {
			Snack snack = (Snack)schedule;
			dto.setIconId(2);
			dto.setKind(snack.getKind());
			return dto;
		}
		if (schedule instanceof Potty) {
			Potty potty = (Potty)schedule;
			dto.setIconId(3);
			dto.setKind(potty.getKind().toString());
			return dto;
		}
		if (schedule instanceof Drug) {
			Drug drug = (Drug)schedule;
			dto.setIconId(4);
			dto.setKind(drug.getKind());
			dto.setPrescriptionUrl(drug.getPrescriptionUrl());
			dto.setExpenses(drug.getExpenses());
			return dto;
		}
		if (schedule instanceof Hospital) {
			Hospital hospital = (Hospital)schedule;
			dto.setIconId(5);
			dto.setDisease(hospital.getDisease());
			dto.setKind(hospital.getKind());
			dto.setPrescriptionUrl(hospital.getPrescriptionUrl());
			dto.setExpenses(hospital.getExpenses());
			dto.setWeight(hospital.getWeight());

			setAddress(dto, hospital.getAddress());
			return dto;
		}
		if (schedule instanceof Salon) {
			Salon salon = (Salon)schedule;
			dto.setIconId(6);
			dto.setExpenses(salon.getExpenses());
			setAddress(dto, salon.getAddress());
			return dto;
		}
		if (schedule instanceof Bath) {
			dto.setIconId(7);
			return dto;
		}
		if (schedule instanceof Sleep) {
			dto.setIconId(8);
			return dto;
		}
		if (schedule instanceof Play) {
			dto.setIconId(9);
			return dto;
		}
		if (schedule instanceof Training) {
			dto.setIconId(10);
			return dto;
		}
		if (schedule instanceof Menstruation) {
			dto.setIconId(11);
			return dto;
		}
		if (schedule instanceof Walk) {
			Walk walk = (Walk)schedule;
			dto.setIconId(12);
			dto.setStartTime(String.valueOf(walk.getStartTime()));
			dto.setEndTime(String.valueOf(walk.getEndTime()));
			return dto;
		}

		// ETC 인 경우
		setEtcSchedule(dto, schedule);
		return dto;
	}

	public static void setBasicInformation(DetailScheduleDto dto, Schedule schedule) {
		dto.setScheduleId(schedule.getId());
		dto.setUserId(schedule.getUser().getId());
		dto.setDate(String.valueOf(schedule.getDate()));
		dto.setMemo(schedule.getMemo());

		if(schedule.getTime() != null) {
			dto.setTime(String.valueOf(schedule.getTime()));
		}

		if (schedule.getDog() != null) {
			dto.setDogId(schedule.getDog().getId());
		}
	}

	public static void setAddress(DetailScheduleDto dto, Address address) {
		if (address != null) {
			dto.setLocationName(address.getLocationName());

			if(address.getAddress() != null) {
				dto.setAddress(address.getAddress());
			}
			if(address.getX() != null && address.getY() != null) {
				dto.setX(address.getX());
				dto.setY(address.getY());
			}
		}
	}

	public static void setEtcSchedule(DetailScheduleDto dto, Schedule schedule) {
		dto.setIconId(13);
		List<EtcItemDto> etcItemDtos = new ArrayList<>();
		for(EtcItem etcItem : schedule.getEtcItems()) {
			etcItemDtos.add(EtcItemDto.convertToEtcItemDto(etcItem));
		}
		dto.setEtcItemDtoList(etcItemDtos);
	}
}
