package com.pet.comes.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.DogLog;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.Entity.schedule.Drug;
import com.pet.comes.model.Entity.schedule.Feeding;
import com.pet.comes.model.Entity.schedule.Hospital;
import com.pet.comes.model.Entity.schedule.Potty;
import com.pet.comes.model.Entity.schedule.Snack;
import com.pet.comes.repository.AddressRepository;
import com.pet.comes.repository.DogLogRepository;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.repository.schedule.DrugRepository;
import com.pet.comes.repository.schedule.FeedingRepository;
import com.pet.comes.repository.schedule.HospitalRepository;
import com.pet.comes.repository.schedule.PottyRepository;
import com.pet.comes.repository.schedule.SnackRepository;
import com.pet.comes.response.NoDataResponse;
import com.pet.comes.response.ResponseMessage;
import com.pet.comes.response.Status;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final FeedingRepository feedingRepository;
	private final SnackRepository snackRepository;
	private final PottyRepository pottyRepository;
	private final DrugRepository drugRepository;
	private final HospitalRepository hospitalRepository;

	private final UserRepository userRepository;
	private final AddressRepository addressRepository;
	private final DogRepository dogRepository;
	private final DogLogRepository dogLogRepository;

	private final Status status;
	private final ResponseMessage responseMessage;

	/* iconId */
	private final int FEEDING = 1;
	private final int SNACK = 2;
	private final int POTTY = 3;
	private final int DRUG = 4;
	private final int HOSPITAL = 5;
	private final int SALON = 6;
	private final int BATH = 7;
	private final int SLEEP = 8;
	private final int PLAYING = 9;
	private final int TRAINING = 10;
	private final int MENSTRUATION = 11;
	private final int WALK = 12;
	private final int ETC = 13;

	public void setDateTimeMemoIfAbsent(Map<String, String> scheduleMap) {
		scheduleMap.putIfAbsent("date", LocalDate.now().toString().substring(0, 10));
		scheduleMap.putIfAbsent("time", LocalTime.now().toString().substring(0, 6) + "00");
		scheduleMap.putIfAbsent("memo", null);
	}

	public void setTimeFormat(Map<String, String> scheduleMap) {
		if (scheduleMap.get("time").length() == 5) {
			scheduleMap.put("time", scheduleMap.get("time") + ":00");
		}
	}

	public void setFeedingParametersIfAbsent(Map<String, String> scheduleMap) {
		scheduleMap.putIfAbsent("kind", null);
		scheduleMap.putIfAbsent("dryOrWet", null);
		scheduleMap.putIfAbsent("amount", null);
	}

	public void setDrugParametersIfAbsent(Map<String, String> scheduleMap) {
		scheduleMap.putIfAbsent("prescriptionUrl", null);
		scheduleMap.putIfAbsent("kind", null);
		scheduleMap.putIfAbsent("expenses", "-1");
	}

	public void saveDogWeightLog(Dog dog, float weight) {
		DogLog dogLog = new DogLog(dog, weight);
		dogLogRepository.save(dogLog);
		dog.addDogLog(dogLog);
	}

	public void updateDogWeight(Dog dog, float weight) {
		dog.setWeight(weight);
		dogRepository.save(dog);
	}

	public void setAddressParametersIfAbsent(Map<String, String> scheduleMap) {
		scheduleMap.putIfAbsent("address", null);
		scheduleMap.putIfAbsent("x", null);
		scheduleMap.putIfAbsent("y", null);
		scheduleMap.putIfAbsent("locationName", null);
	}

	public Address setAddress(Map<String, String> scheduleMap) {
		if (!scheduleMap.containsKey("address") && !scheduleMap.containsKey("x")
			&& !scheduleMap.containsKey("x") && !scheduleMap.containsKey("locationName")) { // 주소가 입력되지 않은 경우
			return null;
		} else { // 주소의 네가지 항목 중 하나라도 입력된 경우
			setAddressParametersIfAbsent(scheduleMap);
			Optional<Address> address = addressRepository.findByLocationName(
				scheduleMap.get("locationName"));
			return address.orElseGet(() -> new Address(scheduleMap));
		}
	}

	public void setHospitalParametersIfAbsent(Map<String, String> scheduleMap) {
		scheduleMap.putIfAbsent("disease", null);
		scheduleMap.putIfAbsent("kind", null);
		scheduleMap.putIfAbsent("prescriptionUrl", null);
		scheduleMap.putIfAbsent("expenses", "-1");
		scheduleMap.putIfAbsent("weight", "-1");
	}

	@Transactional
	public ResponseEntity<String> registerSchedule(Map<String, String> scheduleMap) {
		int iconId = Integer.parseInt(scheduleMap.get("iconId"));

		Optional<User> user = userRepository.findById(Long.parseLong(scheduleMap.get("userId")));
		if (user.isEmpty()) {
			return new ResponseEntity(
				NoDataResponse.response(status.INVALID_ID, "유효하지 않은 userId 입니다. userId : " + scheduleMap.get("userId")),
				HttpStatus.BAD_REQUEST);
		}

		setDateTimeMemoIfAbsent(scheduleMap);
		if (!scheduleMap.get("date").matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
			return new ResponseEntity(
				NoDataResponse.response(status.INVALID_DATE, "잘못된 형식입니다. date(YYYY-MM-DD) : " + scheduleMap.get("date")),
				HttpStatus.BAD_REQUEST);
		}

		setTimeFormat(scheduleMap);
		if (!scheduleMap.get("time").matches("^\\d{2}:([0-5][0-9]):(00)$")) {
			return new ResponseEntity(
				NoDataResponse.response(status.INVALID_TIME, "잘못된 형식입니다. time(HH:MM:00) : " + scheduleMap.get("time")),
				HttpStatus.BAD_REQUEST);
		}

		/* 본격 로직 시작 */
		if (iconId == FEEDING) {
			setFeedingParametersIfAbsent(scheduleMap);
			String dryOrWet = scheduleMap.get("dryOrWet");
			if (!dryOrWet.equals("DRY") && !dryOrWet.equals("WET")) {
				return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
					"dryOrWet 값은 DRY 또는 WET 중 하나여야 합니다. dryOrWet : " + dryOrWet), HttpStatus.OK);
			}

			Feeding feeding = new Feeding(scheduleMap, user.get());
			feedingRepository.save(feeding);
			return new ResponseEntity(
				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE), HttpStatus.OK);
		}
		if (iconId == SNACK) {
			scheduleMap.putIfAbsent("kind", null);
			String kind = scheduleMap.get("kind");
			if (!kind.contains("홈메이드 : ") && !kind.contains("구매 : ")) {
				return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
					"kind 값은 \'홈메이드 : \' 또는 \'구매 : \' 로 시작해야 합니다. kind : " + kind), HttpStatus.OK);
			}
			Snack snack = new Snack(scheduleMap, user.get());
			snackRepository.save(snack);
			return new ResponseEntity(
				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE), HttpStatus.OK);
		}
		if (iconId == POTTY) {
			scheduleMap.putIfAbsent("kind", null);
			String kind = scheduleMap.get("kind");
			if (!kind.equals("URINE") && !kind.equals("FECES")) {
				return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
					"kind 값은 URINE(소변) 또는 FECES(대변) 중 하나여야 합니다. kind : " + kind), HttpStatus.OK);
			}
			Potty potty = new Potty(scheduleMap, user.get());
			pottyRepository.save(potty);
			return new ResponseEntity(
				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE), HttpStatus.OK);
		}
		if (iconId == DRUG) {
			setDrugParametersIfAbsent(scheduleMap);
			Drug drug = new Drug(scheduleMap, user.get());
			drugRepository.save(drug);
			return new ResponseEntity(
				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE), HttpStatus.OK);
		}
		if (iconId == HOSPITAL) {
			if (scheduleMap.containsKey("weight")) { // 몸무게가 입력된 경우
				float afterWeight = Float.parseFloat(scheduleMap.get("weight"));
				if (afterWeight != 0.0f) {
					new ResponseEntity(
						NoDataResponse.response(status.INVALID_VALUE, "유효한 weight 값이 아닙니다. weight : " + afterWeight),
						HttpStatus.OK);
				}
				if (!scheduleMap.containsKey("dogId")) {
					new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "dogId가 입력되지 않았습니다."),
						HttpStatus.OK);
				}
				String dogId = scheduleMap.get("dogId");
				Optional<Dog> foundDog = dogRepository.findById(Long.parseLong(dogId));
				Dog dog = foundDog.orElse(null);
				if (dog == null) {
					return new ResponseEntity(
						NoDataResponse.response(status.INVALID_DOGID, "유효하지 않은 dogId 입니다. dogId : " + dogId),
						HttpStatus.OK);
				}
				float beforeWeight = dog.getWeight();
				if (beforeWeight != afterWeight) {
					saveDogWeightLog(dog, afterWeight);
					updateDogWeight(dog, afterWeight);
				}
			}

			Address address = setAddress(scheduleMap);
			if(address != null) {
				addressRepository.save(address);
			}
			setHospitalParametersIfAbsent(scheduleMap);
			Hospital hospital = new Hospital(scheduleMap, user.get(), address);
			hospitalRepository.save(hospital);
			return new ResponseEntity(
				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE), HttpStatus.OK);
		}
		if (iconId == SALON) {

		}
		if (iconId == BATH) {

		}
		if (iconId == SLEEP) {

		}
		if (iconId == PLAYING) {

		}
		if (iconId == TRAINING) {

		}
		if (iconId == MENSTRUATION) {

		}
		if (iconId == WALK) {

		}
		if (iconId == ETC) {

		}
		return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 iconId 입니다. iconId : " + iconId),
			HttpStatus.OK);

	}
}
