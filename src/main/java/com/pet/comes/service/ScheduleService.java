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
import com.pet.comes.model.Entity.schedule.AdditionalItem;
import com.pet.comes.model.Entity.schedule.Bath;
import com.pet.comes.model.Entity.schedule.Drug;
import com.pet.comes.model.Entity.schedule.Etc;
import com.pet.comes.model.Entity.schedule.Feeding;
import com.pet.comes.model.Entity.schedule.Hospital;
import com.pet.comes.model.Entity.schedule.Menstruation;
import com.pet.comes.model.Entity.schedule.Playing;
import com.pet.comes.model.Entity.schedule.Potty;
import com.pet.comes.model.Entity.schedule.Salon;
import com.pet.comes.model.Entity.schedule.Sleep;
import com.pet.comes.model.Entity.schedule.Snack;
import com.pet.comes.model.Entity.schedule.Training;
import com.pet.comes.model.Entity.schedule.Walk;
import com.pet.comes.repository.AddressRepository;
import com.pet.comes.repository.DogLogRepository;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.repository.schedule.AdditionalItemRepository;
import com.pet.comes.repository.schedule.BathRepository;
import com.pet.comes.repository.schedule.DrugRepository;
import com.pet.comes.repository.schedule.EtcRepository;
import com.pet.comes.repository.schedule.FeedingRepository;
import com.pet.comes.repository.schedule.HospitalRepository;
import com.pet.comes.repository.schedule.MenstruationRepository;
import com.pet.comes.repository.schedule.PlayingRepository;
import com.pet.comes.repository.schedule.PottyRepository;
import com.pet.comes.repository.schedule.SalonRepository;
import com.pet.comes.repository.schedule.SleepRepository;
import com.pet.comes.repository.schedule.SnackRepository;
import com.pet.comes.repository.schedule.TrainingRepository;
import com.pet.comes.repository.schedule.WalkRepository;
import com.pet.comes.response.DataResponse;
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
	private final SalonRepository salonRepository;
	private final BathRepository bathRepository;
	private final SleepRepository sleepRepository;
	private final PlayingRepository playingRepository;
	private final TrainingRepository trainingRepository;
	private final MenstruationRepository menstruationRepository;
	private final WalkRepository walkRepository;
	private final EtcRepository etcRepository;
	private final AdditionalItemRepository additionalItemRepository;

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
		scheduleMap.putIfAbsent("dryOrWet", "DRY");
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

	public void setSalonParametersIfAbsent(Map<String, String> scheduleMap) {
		scheduleMap.putIfAbsent("expenses", "-1");
	}

	public void setWalkParametersIfAbsent(Map<String, String> scheduleMap) {
		LocalTime now = LocalTime.now();
		LocalTime end = now.plusHours(1);
		scheduleMap.putIfAbsent("startTime", now.toString().substring(0, 6) + "00");
		scheduleMap.putIfAbsent("endTime", end.toString().substring(0, 6) + "00");
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
				NoDataResponse.response(status.INVALID_DATE,
					"잘못된 형식입니다. date(YYYY-MM-DD) : " + scheduleMap.get("date")),
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
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, feeding.getId()), HttpStatus.OK);
		}
		if (iconId == SNACK) {
			scheduleMap.putIfAbsent("kind", null);
			String kind = scheduleMap.get("kind");
			if (kind != null && !kind.contains("홈메이드 : ") && !kind.contains("구매 : ")) {
				return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
					"kind 값은 \'홈메이드 : \' 또는 \'구매 : \' 로 시작해야 합니다. kind : " + kind), HttpStatus.OK);
			}
			Snack snack = new Snack(scheduleMap, user.get());
			snackRepository.save(snack);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, snack.getId()), HttpStatus.OK);
		}
		if (iconId == POTTY) {
			scheduleMap.putIfAbsent("kind", "URINE");
			String kind = scheduleMap.get("kind");
			if (!kind.equals("URINE") && !kind.equals("FECES")) {
				return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
					"kind 값은 URINE(소변) 또는 FECES(대변) 중 하나여야 합니다. kind : " + kind), HttpStatus.OK);
			}
			Potty potty = new Potty(scheduleMap, user.get());
			pottyRepository.save(potty);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, potty.getId()), HttpStatus.OK);
		}
		if (iconId == DRUG) {
			setDrugParametersIfAbsent(scheduleMap);
			Drug drug = new Drug(scheduleMap, user.get());
			drugRepository.save(drug);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, drug.getId()), HttpStatus.OK);
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
			if (address != null) {
				addressRepository.save(address);
			}
			setHospitalParametersIfAbsent(scheduleMap);
			Hospital hospital = new Hospital(scheduleMap, user.get(), address);
			hospitalRepository.save(hospital);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, hospital.getId()), HttpStatus.OK);
		}
		if (iconId == SALON) {
			Address address = setAddress(scheduleMap);
			if (address != null) {
				addressRepository.save(address);
			}
			setSalonParametersIfAbsent(scheduleMap);
			Salon salon = new Salon(scheduleMap, user.get(), address);
			salonRepository.save(salon);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, salon.getId()), HttpStatus.OK);
		}
		if (iconId == BATH) {
			Bath bath = new Bath(scheduleMap, user.get());
			bathRepository.save(bath);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, bath.getId()), HttpStatus.OK);
		}
		if (iconId == SLEEP) {
			Sleep sleep = new Sleep(scheduleMap, user.get());
			sleepRepository.save(sleep);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, sleep.getId()), HttpStatus.OK);
		}
		if (iconId == PLAYING) {
			Playing playing = new Playing(scheduleMap, user.get());
			playingRepository.save(playing);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, playing.getId()), HttpStatus.OK);
		}
		if (iconId == TRAINING) {
			Training training = new Training(scheduleMap, user.get());
			trainingRepository.save(training);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, training.getId()), HttpStatus.OK);
		}
		if (iconId == MENSTRUATION) {
			Menstruation menstruation = new Menstruation(scheduleMap, user.get());
			menstruationRepository.save(menstruation);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, menstruation.getId()), HttpStatus.OK);
		}
		if (iconId == WALK) {
			setWalkParametersIfAbsent(scheduleMap); // 기본 1시간 산책
			Walk walk = new Walk(scheduleMap, user.get());
			walkRepository.save(walk);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, walk.getId()), HttpStatus.OK);
		}
		if (iconId == ETC) {
			if(!scheduleMap.containsKey("additionalItemNo")) {
				return new ResponseEntity(
					NoDataResponse.response(status.EMPTY_VALUE, "additionalItemNo " + responseMessage.EMPTY_VALUE), HttpStatus.OK);
			}

			int number = Integer.parseInt(scheduleMap.get("additionalItemNo"));
			if(number == 0) {
				return new ResponseEntity(
					NoDataResponse.response(status.INVALID_VALUE, "additionalItemNo가 0입니다. 추가항목이 있어야 등록 가능합니다. "), HttpStatus.OK);
			}

			Etc etc = new Etc(scheduleMap, user.get());
			etcRepository.save(etc);
			System.out.println(etc.getId());
			AdditionalItem additionalItem;
			String item, value;
			for(int i = 0; i<number; i++) {
				item = scheduleMap.get("item" + i);
				value = scheduleMap.get("value" + i);
				additionalItem = new AdditionalItem(item, value, etc);
				etc.addAdditionalItem(additionalItem);
				additionalItemRepository.save(additionalItem);
			}
			etcRepository.save(etc);
			return new ResponseEntity(
				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, etc.getId()), HttpStatus.OK);
		}

		return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 iconId 입니다. iconId : " + iconId),
			HttpStatus.OK);

	}

	public ResponseEntity<String> modifySchedule(Map<String, String> scheduleMap) {
		Optional<User> user = userRepository.findById(Long.parseLong(scheduleMap.get("userId")));
		if (user.isEmpty()) {
			return new ResponseEntity(
				NoDataResponse.response(status.INVALID_ID, "유효하지 않은 userId 입니다. userId : " + scheduleMap.get("userId")),
				HttpStatus.BAD_REQUEST);
		}

		// 스케줄 가져오기
		int iconId = Integer.parseInt(scheduleMap.get("iconId"));


		// 유저 권한검사 (작성자 본인인지, 가족에 속해있는지)


		return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 iconId 입니다. iconId : " + iconId),
			HttpStatus.OK);
	}

}
