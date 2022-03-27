// package com.pet.comes.service;
//
// import java.time.LocalDate;
// import java.time.LocalDateTime;
// import java.time.LocalTime;
// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.Optional;
//
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.pet.comes.dto.Req.ScheduleDto;
// import com.pet.comes.dto.Req.ScheduleListDto;
// import com.pet.comes.model.Entity.Address;
// import com.pet.comes.model.Entity.Dog;
// import com.pet.comes.model.Entity.DogLog;
// import com.pet.comes.model.Entity.User;
// import com.pet.comes.model.Entity.schedule.AdditionalItem;
// import com.pet.comes.model.Entity.schedule.Bath;
// import com.pet.comes.model.Entity.schedule.Drug;
// import com.pet.comes.model.Entity.schedule.Etc;
// import com.pet.comes.model.Entity.schedule.Feeding;
// import com.pet.comes.model.Entity.schedule.Hospital;
// import com.pet.comes.model.Entity.schedule.Menstruation;
// import com.pet.comes.model.Entity.schedule.Playing;
// import com.pet.comes.model.Entity.schedule.Potty;
// import com.pet.comes.model.Entity.schedule.Salon;
// import com.pet.comes.model.Entity.schedule.Sleep;
// import com.pet.comes.model.Entity.schedule.Snack;
// import com.pet.comes.model.Entity.schedule.Training;
// import com.pet.comes.model.Entity.schedule.Walk;
// import com.pet.comes.model.EnumType.DryOrWetFeed;
// import com.pet.comes.model.EnumType.KindOfPotty;
// import com.pet.comes.repository.AddressRepository;
// import com.pet.comes.repository.DogLogRepository;
// import com.pet.comes.repository.DogRepository;
// import com.pet.comes.repository.UserRepository;
// import com.pet.comes.repository.schedule.AdditionalItemRepository;
// import com.pet.comes.repository.schedule.BathRepository;
// import com.pet.comes.repository.schedule.DrugRepository;
// import com.pet.comes.repository.schedule.EtcRepository;
// import com.pet.comes.repository.schedule.FeedingRepository;
// import com.pet.comes.repository.schedule.HospitalRepository;
// import com.pet.comes.repository.schedule.MenstruationRepository;
// import com.pet.comes.repository.schedule.PlayingRepository;
// import com.pet.comes.repository.schedule.PottyRepository;
// import com.pet.comes.repository.schedule.SalonRepository;
// import com.pet.comes.repository.schedule.SleepRepository;
// import com.pet.comes.repository.schedule.SnackRepository;
// import com.pet.comes.repository.schedule.TrainingRepository;
// import com.pet.comes.repository.schedule.WalkRepository;
// import com.pet.comes.response.DataResponse;
// import com.pet.comes.response.NoDataResponse;
// import com.pet.comes.response.ResponseMessage;
// import com.pet.comes.response.Status;
//
// import lombok.RequiredArgsConstructor;
//
// @Service
// @RequiredArgsConstructor
// public class ScheduleService {
//
// 	private final FeedingRepository feedingRepository;
// 	private final SnackRepository snackRepository;
// 	private final PottyRepository pottyRepository;
// 	private final DrugRepository drugRepository;
// 	private final HospitalRepository hospitalRepository;
// 	private final SalonRepository salonRepository;
// 	private final BathRepository bathRepository;
// 	private final SleepRepository sleepRepository;
// 	private final PlayingRepository playingRepository;
// 	private final TrainingRepository trainingRepository;
// 	private final MenstruationRepository menstruationRepository;
// 	private final WalkRepository walkRepository;
// 	private final EtcRepository etcRepository;
// 	private final AdditionalItemRepository additionalItemRepository;
//
// 	private final UserRepository userRepository;
// 	private final AddressRepository addressRepository;
// 	private final DogRepository dogRepository;
// 	private final DogLogRepository dogLogRepository;
//
// 	private final Status status;
// 	private final ResponseMessage responseMessage;
//
// 	/* iconId */
// 	private final int FEEDING = 1;
// 	private final int SNACK = 2;
// 	private final int POTTY = 3;
// 	private final int DRUG = 4;
// 	private final int HOSPITAL = 5;
// 	private final int SALON = 6;
// 	private final int BATH = 7;
// 	private final int SLEEP = 8;
// 	private final int PLAYING = 9;
// 	private final int TRAINING = 10;
// 	private final int MENSTRUATION = 11;
// 	private final int WALK = 12;
// 	private final int ETC = 13;
//
// 	/**
// 	 * W1 : 일정 등록
// 	 * */
// 	@Transactional
// 	public ResponseEntity<String> registerSchedule(Map<String, String> scheduleMap) {
// 		int iconId = Integer.parseInt(scheduleMap.get("iconId"));
//
// 		Optional<User> user = userRepository.findById(Long.parseLong(scheduleMap.get("userId")));
// 		if (user.isEmpty()) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.INVALID_ID, "유효하지 않은 userId 입니다. userId : " + scheduleMap.get("userId")),
// 				HttpStatus.BAD_REQUEST);
// 		}
//
// 		setDateTimeMemoIfAbsent(scheduleMap);
// 		if (!scheduleMap.get("date").matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.INVALID_DATE,
// 					"잘못된 형식입니다. date(YYYY-MM-DD) : " + scheduleMap.get("date")),
// 				HttpStatus.BAD_REQUEST);
// 		}
//
// 		setTimeFormat(scheduleMap);
// 		if (!scheduleMap.get("time").matches("^\\d{2}:([0-5][0-9]):(00)$")) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.INVALID_TIME, "잘못된 형식입니다. time(HH:MM:00) : " + scheduleMap.get("time")),
// 				HttpStatus.BAD_REQUEST);
// 		}
//
// 		/* 본격 로직 시작 */
// 		if (iconId == FEEDING) {
// 			setFeedingParametersIfAbsent(scheduleMap);
// 			String dryOrWet = scheduleMap.get("dryOrWet");
// 			if (!dryOrWet.equals("DRY") && !dryOrWet.equals("WET")) {
// 				return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
// 					"dryOrWet 값은 DRY 또는 WET 중 하나여야 합니다. dryOrWet : " + dryOrWet), HttpStatus.OK);
// 			}
// 			Feeding feeding = new Feeding(scheduleMap, user.get());
// 			feedingRepository.save(feeding);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, feeding.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == SNACK) {
// 			scheduleMap.putIfAbsent("kind", null);
// 			String kind = scheduleMap.get("kind");
// 			if (kind != null && !kind.contains("홈메이드 : ") && !kind.contains("구매 : ")) {
// 				return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
// 					"kind 값은 \'홈메이드 : \' 또는 \'구매 : \' 로 시작해야 합니다. kind : " + kind), HttpStatus.OK);
// 			}
// 			Snack snack = new Snack(scheduleMap, user.get());
// 			snackRepository.save(snack);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, snack.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == POTTY) {
// 			scheduleMap.putIfAbsent("kind", "URINE");
// 			String kind = scheduleMap.get("kind");
// 			if (!kind.equals("URINE") && !kind.equals("FECES")) {
// 				return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
// 					"kind 값은 URINE(소변) 또는 FECES(대변) 중 하나여야 합니다. kind : " + kind), HttpStatus.OK);
// 			}
// 			Potty potty = new Potty(scheduleMap, user.get());
// 			pottyRepository.save(potty);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, potty.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == DRUG) {
// 			setDrugParametersIfAbsent(scheduleMap);
// 			Drug drug = new Drug(scheduleMap, user.get());
// 			drugRepository.save(drug);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, drug.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == HOSPITAL) {
// 			if (scheduleMap.containsKey("weight")) { // 몸무게가 입력된 경우
// 				float afterWeight = Float.parseFloat(scheduleMap.get("weight"));
// 				if (afterWeight <= 0.0f) {
// 					return new ResponseEntity(
// 						NoDataResponse.response(status.INVALID_VALUE, "유효한 weight 값이 아닙니다. weight : " + afterWeight),
// 						HttpStatus.OK);
// 				}
// 				if (!scheduleMap.containsKey("dogId")) {
// 					return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "dogId가 입력되지 않았습니다."),
// 						HttpStatus.OK);
// 				}
// 				String dogId = scheduleMap.get("dogId");
// 				Optional<Dog> foundDog = dogRepository.findById(Long.parseLong(dogId));
// 				Dog dog = foundDog.orElse(null);
// 				if (dog == null) {
// 					return new ResponseEntity(
// 						NoDataResponse.response(status.INVALID_DOGID, "유효하지 않은 dogId 입니다. dogId : " + dogId),
// 						HttpStatus.OK);
// 				}
// 				float beforeWeight = dog.getWeight();
// 				if (beforeWeight != afterWeight) {
// 					saveDogWeightLog(dog, afterWeight);
// 					updateDogWeight(dog, afterWeight);
// 				}
// 			}
//
// 			Address address = setAddress(scheduleMap);
// 			if (address != null) {
// 				addressRepository.save(address);
// 			}
// 			setHospitalParametersIfAbsent(scheduleMap);
// 			Hospital hospital = new Hospital(scheduleMap, user.get(), address);
// 			hospitalRepository.save(hospital);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, hospital.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == SALON) {
// 			Address address = setAddress(scheduleMap);
// 			if (address != null) {
// 				addressRepository.save(address);
// 			}
// 			setSalonParametersIfAbsent(scheduleMap);
// 			Salon salon = new Salon(scheduleMap, user.get(), address);
// 			salonRepository.save(salon);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, salon.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == BATH) {
// 			Bath bath = new Bath(scheduleMap, user.get());
// 			bathRepository.save(bath);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, bath.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == SLEEP) {
// 			Sleep sleep = new Sleep(scheduleMap, user.get());
// 			sleepRepository.save(sleep);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, sleep.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == PLAYING) {
// 			Playing playing = new Playing(scheduleMap, user.get());
// 			playingRepository.save(playing);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, playing.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == TRAINING) {
// 			Training training = new Training(scheduleMap, user.get());
// 			trainingRepository.save(training);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, training.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == MENSTRUATION) {
// 			Menstruation menstruation = new Menstruation(scheduleMap, user.get());
// 			menstruationRepository.save(menstruation);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, menstruation.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == WALK) {
// 			setWalkParametersIfAbsent(scheduleMap); // 기본 1시간 산책
// 			Walk walk = new Walk(scheduleMap, user.get());
// 			walkRepository.save(walk);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, walk.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == ETC) {
// 			if (!scheduleMap.containsKey("additionalItemNo")) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.EMPTY_VALUE, "additionalItemNo " + responseMessage.EMPTY_VALUE),
// 					HttpStatus.OK);
// 			}
//
// 			int number = Integer.parseInt(scheduleMap.get("additionalItemNo"));
// 			if (number == 0) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_VALUE, "additionalItemNo가 0입니다. 추가항목이 있어야 등록 가능합니다. "),
// 					HttpStatus.OK);
// 			}
//
// 			Etc etc = new Etc(scheduleMap, user.get());
// 			etcRepository.save(etc);
// 			System.out.println(etc.getId());
// 			AdditionalItem additionalItem;
// 			String item, value;
// 			for (int i = 0; i < number; i++) {
// 				item = scheduleMap.get("item" + i);
// 				value = scheduleMap.get("value" + i);
// 				additionalItem = new AdditionalItem(item, value, etc);
// 				etc.addAdditionalItem(additionalItem);
// 				additionalItemRepository.save(additionalItem);
// 			}
// 			etcRepository.save(etc);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_REGISTER_SCHEDULE, etc.getId()),
// 				HttpStatus.OK);
// 		}
//
// 		return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 iconId 입니다. iconId : " + iconId),
// 			HttpStatus.OK);
//
// 	}
//
// 	public void setDateTimeMemoIfAbsent(Map<String, String> scheduleMap) {
// 		scheduleMap.putIfAbsent("date", LocalDate.now().toString().substring(0, 10));
// 		scheduleMap.putIfAbsent("time", LocalTime.now().toString().substring(0, 6) + "00");
// 		scheduleMap.putIfAbsent("memo", null);
// 	}
//
// 	public void setTimeFormat(Map<String, String> scheduleMap) {
// 		if (scheduleMap.get("time").length() == 5) {
// 			scheduleMap.put("time", scheduleMap.get("time") + ":00");
// 		}
// 	}
//
// 	public void setFeedingParametersIfAbsent(Map<String, String> scheduleMap) {
// 		scheduleMap.putIfAbsent("kind", null);
// 		scheduleMap.putIfAbsent("dryOrWet", "DRY");
// 		scheduleMap.putIfAbsent("amount", null);
// 	}
//
// 	public void setDrugParametersIfAbsent(Map<String, String> scheduleMap) {
// 		scheduleMap.putIfAbsent("prescriptionUrl", null);
// 		scheduleMap.putIfAbsent("kind", null);
// 		scheduleMap.putIfAbsent("expenses", "-1");
// 	}
//
// 	public void saveDogWeightLog(Dog dog, float weight) {
// 		DogLog dogLog = new DogLog(dog, weight);
// 		dogLogRepository.save(dogLog);
// 		dog.addDogLog(dogLog);
// 	}
//
// 	public void updateDogWeight(Dog dog, float weight) {
// 		dog.setWeight(weight);
// 		dogRepository.save(dog);
// 	}
//
// 	public void setAddressParametersIfAbsent(Map<String, String> scheduleMap) {
// 		scheduleMap.putIfAbsent("address", null);
// 		scheduleMap.putIfAbsent("x", null);
// 		scheduleMap.putIfAbsent("y", null);
// 		scheduleMap.putIfAbsent("locationName", null);
// 	}
//
// 	public Address setAddress(Map<String, String> scheduleMap) {
// 		if (!scheduleMap.containsKey("address") && !scheduleMap.containsKey("x")
// 			&& !scheduleMap.containsKey("x") && !scheduleMap.containsKey("locationName")) { // 주소가 입력되지 않은 경우
// 			return null;
// 		} else { // 주소의 네가지 항목 중 하나라도 입력된 경우
// 			setAddressParametersIfAbsent(scheduleMap);
// 			Optional<Address> address = addressRepository.findByLocationName(
// 				scheduleMap.get("locationName"));
// 			return address.orElseGet(() -> new Address(scheduleMap));
// 		}
// 	}
//
// 	public void setHospitalParametersIfAbsent(Map<String, String> scheduleMap) {
// 		scheduleMap.putIfAbsent("disease", null);
// 		scheduleMap.putIfAbsent("kind", null);
// 		scheduleMap.putIfAbsent("prescriptionUrl", null);
// 		scheduleMap.putIfAbsent("expenses", "-1");
// 		scheduleMap.putIfAbsent("weight", "-1");
// 	}
//
// 	public void setSalonParametersIfAbsent(Map<String, String> scheduleMap) {
// 		scheduleMap.putIfAbsent("expenses", "-1");
// 	}
//
// 	public void setWalkParametersIfAbsent(Map<String, String> scheduleMap) {
// 		LocalTime now = LocalTime.now();
// 		LocalTime end = now.plusHours(1);
// 		scheduleMap.putIfAbsent("startTime", now.toString().substring(0, 6) + "00");
// 		scheduleMap.putIfAbsent("endTime", end.toString().substring(0, 6) + "00");
// 	}
//
// 	/**
// 	 * W3 : 일정 수정
// 	 * */
// 	public ResponseEntity<String> modifySchedule(Map<String, String> scheduleMap) {
// 		if (!containsKey(scheduleMap, "userId")) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.EMPTY_VALUE, "userId가 입력되지 않았습니다."),
// 				HttpStatus.BAD_REQUEST);
// 		}
// 		if (!containsKey(scheduleMap, "iconId")) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.EMPTY_VALUE, "iconId가 입력되지 않았습니다."),
// 				HttpStatus.BAD_REQUEST);
// 		}
// 		if (!containsKey(scheduleMap, "scheduleId")) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.EMPTY_VALUE, "scheduleId가 입력되지 않았습니다."),
// 				HttpStatus.BAD_REQUEST);
// 		}
//
// 		int userId = Integer.parseInt(scheduleMap.get("userId"));
// 		int iconId = Integer.parseInt(scheduleMap.get("iconId"));
// 		int scheduleId = Integer.parseInt(scheduleMap.get("scheduleId"));
// 		LocalDate date = null;
// 		LocalTime time = null;
//
// 		Optional<User> optionalUser = userRepository.findById(Long.parseLong(scheduleMap.get("userId")));
// 		if (optionalUser.isEmpty()) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.INVALID_ID, "유효하지 않은 userId 입니다. userId : " + userId),
// 				HttpStatus.BAD_REQUEST);
// 		}
// 		User user = optionalUser.get();
//
// 		if (containsKey(scheduleMap, "date")) {
// 			if (!scheduleMap.get("date").matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_DATE,
// 						"잘못된 형식입니다. date(YYYY-MM-DD) : " + scheduleMap.get("date")),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			date = LocalDate.parse(scheduleMap.get("date"), DateTimeFormatter.ISO_DATE);
// 		}
//
// 		if (containsKey(scheduleMap, "time")) {
// 			setTimeFormat(scheduleMap);
// 			if (!scheduleMap.get("time").matches("^\\d{2}:([0-5][0-9]):(00)$")) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_TIME,
// 						"잘못된 형식입니다. time(HH:MM:00) : " + scheduleMap.get("time")),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			time = LocalTime.parse(scheduleMap.get("time"));
// 		}
//
// 		/* 로직 시작 */
// 		if (iconId == FEEDING) {
// 			Optional<Feeding> feeding = feedingRepository.findById((long)scheduleId);
// 			if (feeding.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Feeding feed = feeding.get();
// 			long familyIdOfWriter = feed.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
//
// 			feed.setUser(user);
// 			if (date != null) {
// 				feed.setDate(date);
// 			}
// 			if (time != null) {
// 				feed.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				feed.setMemo(scheduleMap.get("memo"));
// 			}
// 			if (containsKey(scheduleMap, "kind")) {
// 				feed.setKind(scheduleMap.get("kind"));
// 			}
// 			if (containsKey(scheduleMap, "dryOrWet")) {
// 				if (!scheduleMap.get("dryOrWet").equals("DRY") && !scheduleMap.get("dryOrWet").equals("WET")) {
// 					return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
// 						"dryOrWet 값은 DRY 또는 WET 중 하나여야 합니다. dryOrWet : " + scheduleMap.get("dryOrWet")),
// 						HttpStatus.OK);
// 				}
// 				feed.setDryOrWet(DryOrWetFeed.valueOf(scheduleMap.get("dryOrWet")));
// 			}
// 			if (containsKey(scheduleMap, "amount")) {
// 				feed.setAmount(scheduleMap.get("amount"));
// 			}
// 			feed.setModifiedAt(LocalDateTime.now());
// 			feedingRepository.save(feed);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, feed.getId()),
// 				HttpStatus.OK);
//
// 		}
// 		if (iconId == SNACK) {
// 			Optional<Snack> optionalSnack = snackRepository.findById((long)scheduleId);
// 			if (optionalSnack.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Snack snack = optionalSnack.get();
// 			long familyIdOfWriter = snack.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
//
// 			snack.setUser(user);
// 			if (date != null) {
// 				snack.setDate(date);
// 			}
// 			if (time != null) {
// 				snack.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				snack.setMemo(scheduleMap.get("memo"));
// 			}
// 			if (containsKey(scheduleMap, "kind")) {
// 				snack.setKind(scheduleMap.get("kind"));
// 			}
// 			snack.setModifiedAt(LocalDateTime.now());
// 			snackRepository.save(snack);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, snack.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == POTTY) {
// 			Optional<Potty> optionalPotty = pottyRepository.findById((long)scheduleId);
// 			if (optionalPotty.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Potty potty = optionalPotty.get();
// 			long familyIdOfWriter = potty.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
//
// 			potty.setUser(user);
// 			if (date != null) {
// 				potty.setDate(date);
// 			}
// 			if (time != null) {
// 				potty.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				potty.setMemo(scheduleMap.get("memo"));
// 			}
// 			if (containsKey(scheduleMap, "kind")) {
// 				if (!scheduleMap.get("kind").equals("URINE") && !scheduleMap.get("kind").equals("FECES")) {
// 					return new ResponseEntity(NoDataResponse.response(status.INVALID_VALUE,
// 						"kind 값은 URINE 또는 FECES 중 하나여야 합니다. kind : " + scheduleMap.get("kind")),
// 						HttpStatus.OK);
// 				}
// 				potty.setKind(KindOfPotty.valueOf(scheduleMap.get("kind")));
// 			}
// 			potty.setModifiedAt(LocalDateTime.now());
// 			pottyRepository.save(potty);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, potty.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == DRUG) {
// 			Optional<Drug> optionalDrug = drugRepository.findById((long)scheduleId);
// 			if (optionalDrug.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Drug drug = optionalDrug.get();
// 			long familyIdOfWriter = drug.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
//
// 			drug.setUser(user);
// 			if (date != null) {
// 				drug.setDate(date);
// 			}
// 			if (time != null) {
// 				drug.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				drug.setMemo(scheduleMap.get("memo"));
// 			}
// 			if (containsKey(scheduleMap, "kind")) {
// 				drug.setKind(scheduleMap.get("kind"));
// 			}
// 			if (containsKey(scheduleMap, "prescriptionUrl")) {
// 				drug.setPrescriptionUrl(scheduleMap.get("prescriptionUrl"));
// 			}
// 			if (containsKey(scheduleMap, "expenses")) {
// 				drug.setExpenses(Integer.parseInt(scheduleMap.get("expenses")));
// 			}
// 			drug.setModifiedAt(LocalDateTime.now());
// 			drugRepository.save(drug);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, drug.getId()),
// 				HttpStatus.OK);
//
// 		}
// 		if (iconId == HOSPITAL) {
// 			Optional<Hospital> optionalHospital = hospitalRepository.findById((long)scheduleId);
// 			if (optionalHospital.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Hospital hospital = optionalHospital.get();
// 			long familyIdOfWriter = hospital.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
//
// 			hospital.setUser(user);
// 			if (date != null) {
// 				hospital.setDate(date);
// 			}
// 			if (time != null) {
// 				hospital.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				hospital.setMemo(scheduleMap.get("memo"));
// 			}
// 			if (containsKey(scheduleMap, "disease")) {
// 				hospital.setDisease(scheduleMap.get("disease"));
// 			}
// 			if (containsKey(scheduleMap, "kind")) {
// 				hospital.setKind(scheduleMap.get("kind"));
// 			}
// 			if (containsKey(scheduleMap, "prescriptionUrl")) {
// 				hospital.setPrescriptionUrl(scheduleMap.get("prescriptionUrl"));
// 			}
// 			if (containsKey(scheduleMap, "expenses")) {
// 				hospital.setExpenses(Integer.parseInt(scheduleMap.get("expenses")));
// 			}
// 			Address address = setAddress(scheduleMap);
// 			if (address != null) {
// 				addressRepository.save(address);
// 				hospital.setAddress(address);
// 			}
// 			if (scheduleMap.containsKey("weight")) { // 몸무게가 입력된 경우
// 				float afterWeight = Float.parseFloat(scheduleMap.get("weight"));
// 				if (afterWeight <= 0.0f) {
// 					return new ResponseEntity(
// 						NoDataResponse.response(status.INVALID_VALUE, "유효한 weight 값이 아닙니다. weight : " + afterWeight),
// 						HttpStatus.OK);
// 				}
// 				if (!scheduleMap.containsKey("dogId")) {
// 					return new ResponseEntity(NoDataResponse.response(status.INVALID_DOGID, "dogId가 입력되지 않았습니다."),
// 						HttpStatus.OK);
// 				}
// 				String dogId = scheduleMap.get("dogId");
// 				Optional<Dog> foundDog = dogRepository.findById(Long.parseLong(dogId));
// 				Dog dog = foundDog.orElse(null);
// 				if (dog == null) {
// 					return new ResponseEntity(
// 						NoDataResponse.response(status.INVALID_DOGID, "유효하지 않은 dogId 입니다. dogId : " + dogId),
// 						HttpStatus.OK);
// 				}
// 				float beforeWeight = dog.getWeight();
// 				if (beforeWeight != afterWeight) {
// 					saveDogWeightLog(dog, afterWeight);
// 					updateDogWeight(dog, afterWeight);
// 				}
// 				hospital.setWeight(afterWeight);
// 			}
// 			hospital.setModifiedAt(LocalDateTime.now());
// 			hospitalRepository.save(hospital);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, hospital.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == SALON) {
// 			Optional<Salon> optionalSalon = salonRepository.findById((long)scheduleId);
// 			if (optionalSalon.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Salon salon = optionalSalon.get();
// 			long familyIdOfWriter = salon.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			salon.setUser(user);
// 			if (date != null) {
// 				salon.setDate(date);
// 			}
// 			if (time != null) {
// 				salon.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				salon.setMemo(scheduleMap.get("memo"));
// 			}
// 			if (containsKey(scheduleMap, "expenses")) {
// 				salon.setExpenses(Integer.parseInt(scheduleMap.get("expenses")));
// 			}
// 			Address address = setAddress(scheduleMap);
// 			if (address != null) {
// 				addressRepository.save(address);
// 				salon.setAddress(address);
// 			}
// 			salon.setModifiedAt(LocalDateTime.now());
// 			salonRepository.save(salon);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, salon.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == BATH) {
// 			Optional<Bath> optionalBath = bathRepository.findById((long)scheduleId);
// 			if (optionalBath.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Bath bath = optionalBath.get();
// 			long familyIdOfWriter = bath.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			bath.setUser(user);
// 			if (date != null) {
// 				bath.setDate(date);
// 			}
// 			if (time != null) {
// 				bath.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				bath.setMemo(scheduleMap.get("memo"));
// 			}
// 			bath.setModifiedAt(LocalDateTime.now());
// 			bathRepository.save(bath);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, bath.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == SLEEP) {
// 			Optional<Sleep> optionalSleep = sleepRepository.findById((long)scheduleId);
// 			if (optionalSleep.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Sleep sleep = optionalSleep.get();
// 			long familyIdOfWriter = sleep.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			sleep.setUser(user);
// 			if (date != null) {
// 				sleep.setDate(date);
// 			}
// 			if (time != null) {
// 				sleep.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				sleep.setMemo(scheduleMap.get("memo"));
// 			}
// 			sleep.setModifiedAt(LocalDateTime.now());
// 			sleepRepository.save(sleep);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, sleep.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == PLAYING) {
// 			Optional<Playing> optionalPlaying = playingRepository.findById((long)scheduleId);
// 			if (optionalPlaying.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Playing playing = optionalPlaying.get();
// 			long familyIdOfWriter = playing.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			playing.setUser(user);
// 			if (date != null) {
// 				playing.setDate(date);
// 			}
// 			if (time != null) {
// 				playing.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				playing.setMemo(scheduleMap.get("memo"));
// 			}
// 			playing.setModifiedAt(LocalDateTime.now());
// 			playingRepository.save(playing);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, playing.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == TRAINING) {
// 			Optional<Training> optionalTraining = trainingRepository.findById((long)scheduleId);
// 			if (optionalTraining.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Training training = optionalTraining.get();
// 			long familyIdOfWriter = training.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			training.setUser(user);
// 			if (date != null) {
// 				training.setDate(date);
// 			}
// 			if (time != null) {
// 				training.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				training.setMemo(scheduleMap.get("memo"));
// 			}
// 			training.setModifiedAt(LocalDateTime.now());
// 			trainingRepository.save(training);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, training.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == MENSTRUATION) {
// 			Optional<Menstruation> optionalMenstruation = menstruationRepository.findById((long)scheduleId);
// 			if (optionalMenstruation.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Menstruation menstruation = optionalMenstruation.get();
// 			long familyIdOfWriter = menstruation.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			menstruation.setUser(user);
// 			if (date != null) {
// 				menstruation.setDate(date);
// 			}
// 			if (time != null) {
// 				menstruation.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				menstruation.setMemo(scheduleMap.get("memo"));
// 			}
// 			menstruation.setModifiedAt(LocalDateTime.now());
// 			menstruationRepository.save(menstruation);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, menstruation.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == WALK) {
// 			Optional<Walk> optionalWalk = walkRepository.findById((long)scheduleId);
// 			if (optionalWalk.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Walk walk = optionalWalk.get();
// 			long familyIdOfWriter = walk.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			walk.setUser(user);
// 			if (date != null) {
// 				walk.setDate(date);
// 			}
// 			if (containsKey(scheduleMap, "startTime") && containsKey(scheduleMap, "endTime")) {
// 				LocalTime startTime = LocalTime.parse(scheduleMap.get("startTime"));
// 				LocalTime endTime = LocalTime.parse(scheduleMap.get("endTime"));
// 				if (startTime.isAfter(endTime)) {
// 					return new ResponseEntity(
// 						NoDataResponse.response(status.INVALID_TIME,
// 							"시작 시간은 끝나는 시간보다 빨라야 합니다. startTime : " + startTime + ", endTime : " + endTime),
// 						HttpStatus.BAD_REQUEST);
// 				}
// 				walk.setStartTime(startTime);
// 				walk.setEndTime(endTime);
// 			} else {
// 				if (containsKey(scheduleMap, "startTime")) {
// 					walk.setStartTime(LocalTime.parse(scheduleMap.get("startTime")));
// 				}
// 				if (containsKey(scheduleMap, "endTime")) {
// 					walk.setEndTime(LocalTime.parse(scheduleMap.get("endTime")));
// 				}
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				walk.setMemo(scheduleMap.get("memo"));
// 			}
// 			walk.setModifiedAt(LocalDateTime.now());
// 			walkRepository.save(walk);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, walk.getId()),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == ETC) {
// 			Optional<Etc> optionalEtc = etcRepository.findById((long)scheduleId);
// 			if (optionalEtc.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Etc etc = optionalEtc.get();
// 			long familyIdOfWriter = etc.getUser().getFamily().getId();
// 			long familyIdOfModifier = user.getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "수정 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			etc.setUser(user);
// 			if (date != null) {
// 				etc.setDate(date);
// 			}
// 			if (time != null) {
// 				etc.setTime(time);
// 			}
// 			if (containsKey(scheduleMap, "memo")) {
// 				etc.setMemo(scheduleMap.get("memo"));
// 			}
//
// 			List<AdditionalItem> additionalItemList = additionalItemRepository.findAdditionalItemsByEtc(etc);
// 			if (scheduleMap.containsKey("delete")) { // 삭제할 항목 구분자 ',' 사용
// 				String[] itemListToDelete = scheduleMap.get("delete").split(",");
// 				for (String item : itemListToDelete) {
// 					Optional<AdditionalItem> optionalItem = additionalItemList.stream()
// 						.filter(x -> x.getItem().equals(item)).findFirst();
//
// 					if (optionalItem.isEmpty()) {
// 						return new ResponseEntity(
// 							NoDataResponse.response(status.INVALID_ITEM, "유효하지 않은 item 입니다. item : " + item),
// 							HttpStatus.BAD_REQUEST);
// 					}
// 					AdditionalItem itemToDelete = optionalItem.get();
// 					etc.getAdditionalItemList().remove(itemToDelete);
// 					additionalItemRepository.delete(itemToDelete);
// 				}
// 			}
// 			if (scheduleMap.containsKey("addItemNo")) { // 추가할 아이템 개수
// 				int numberOfItemToAdd = Integer.parseInt(scheduleMap.get("addItemNo"));
// 				for (int i = 0; i < numberOfItemToAdd; i++) {
// 					AdditionalItem additionalItem = new AdditionalItem(scheduleMap.get("addItem" + i),
// 						scheduleMap.get("addValue" + i), etc);
// 					additionalItemRepository.save(additionalItem);
// 				}
// 			}
// 			if (scheduleMap.containsKey("modifyItemNo")) { // 수정할 아이템 개수
// 				int numberOfItemToModify = Integer.parseInt(scheduleMap.get("modifyItemNo"));
// 				for (int i = 0; i < numberOfItemToModify; i++) {
// 					String key = scheduleMap.get("modifyItem" + i);
// 					Optional<AdditionalItem> optionalItem = additionalItemList.stream()
// 						.filter(x -> x.getItem().equals(key)).findFirst();
//
// 					if (optionalItem.isEmpty()) {
// 						return new ResponseEntity(
// 							NoDataResponse.response(status.INVALID_ITEM,
// 								"유효하지 않은 item 입니다. modifyItem" + (i + 1) + " : " + key),
// 							HttpStatus.BAD_REQUEST);
// 					}
// 					AdditionalItem itemToModify = optionalItem.get();
// 					itemToModify.setValue(scheduleMap.get("modifyValue" + i));
// 					additionalItemRepository.save(itemToModify);
// 				}
// 			}
// 			etc.setModifiedAt(LocalDateTime.now());
// 			etcRepository.save(etc);
// 			return new ResponseEntity(
// 				DataResponse.response(status.SUCCESS, responseMessage.SUCCESS_MODIFY_SCHEDULE, etc.getId()),
// 				HttpStatus.OK);
// 		}
//
// 		return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 iconId 입니다. iconId : " + iconId),
// 			HttpStatus.OK);
// 	}
//
// 	public boolean containsKey(Map<String, String> map, String value) {
// 		return map.containsKey(value);
// 	}
//
// 	/**
// 	 * W4 : 일정 삭제
// 	 * */
// 	public ResponseEntity<String> deleteSchedule(ScheduleDto scheduleDto) {
// 		if (scheduleDto.getUserId() == null) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.EMPTY_VALUE, "userId가 입력되지 않았습니다."),
// 				HttpStatus.BAD_REQUEST);
// 		}
// 		if (scheduleDto.getIconId() == null) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.EMPTY_VALUE, "iconId가 입력되지 않았습니다."),
// 				HttpStatus.BAD_REQUEST);
// 		}
// 		if (scheduleDto.getScheduleId() == null) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.EMPTY_VALUE, "scheduleId가 입력되지 않았습니다."),
// 				HttpStatus.BAD_REQUEST);
// 		}
//
// 		long userId = scheduleDto.getUserId();
// 		long iconId = scheduleDto.getIconId();
// 		long scheduleId = scheduleDto.getScheduleId();
//
// 		Optional<User> optionalUser = userRepository.findById(userId);
// 		if (optionalUser.isEmpty()) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.INVALID_ID, "유효하지 않은 userId 입니다. userId : " + userId),
// 				HttpStatus.BAD_REQUEST);
// 		}
// 		User user = optionalUser.get();
// 		long familyIdOfModifier = user.getFamily().getId();
//
// 		if (iconId == FEEDING) {
// 			Optional<Feeding> feeding = feedingRepository.findById(scheduleId);
// 			if (feeding.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Feeding feed = feeding.get();
// 			long familyIdOfWriter = feed.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			feedingRepository.delete(feed);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
//
// 		}
// 		if (iconId == SNACK) {
// 			Optional<Snack> optionalSnack = snackRepository.findById(scheduleId);
// 			if (optionalSnack.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Snack snack = optionalSnack.get();
// 			long familyOfWriter = snack.getUser().getFamily().getId();
// 			if (familyOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			snackRepository.delete(snack);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == POTTY) {
// 			Optional<Potty> optionalPotty = pottyRepository.findById(scheduleId);
// 			if (optionalPotty.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Potty potty = optionalPotty.get();
// 			long familyIdOfWriter = potty.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			pottyRepository.delete(potty);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == DRUG) {
// 			Optional<Drug> optionalDrug = drugRepository.findById(scheduleId);
// 			if (optionalDrug.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Drug drug = optionalDrug.get();
// 			long familyIdOfWriter = drug.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			drugRepository.delete(drug);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
//
// 		}
// 		if (iconId == HOSPITAL) {
// 			Optional<Hospital> optionalHospital = hospitalRepository.findById(scheduleId);
// 			if (optionalHospital.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Hospital hospital = optionalHospital.get();
// 			long familyIdOfWriter = hospital.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			hospitalRepository.delete(hospital);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == SALON) {
// 			Optional<Salon> optionalSalon = salonRepository.findById(scheduleId);
// 			if (optionalSalon.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Salon salon = optionalSalon.get();
// 			long familyIdOfWriter = salon.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			salonRepository.delete(salon);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == BATH) {
// 			Optional<Bath> optionalBath = bathRepository.findById(scheduleId);
// 			if (optionalBath.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Bath bath = optionalBath.get();
// 			long familyIdOfWriter = bath.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			bathRepository.delete(bath);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == SLEEP) {
// 			Optional<Sleep> optionalSleep = sleepRepository.findById(scheduleId);
// 			if (optionalSleep.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Sleep sleep = optionalSleep.get();
// 			long familyIdOfWriter = sleep.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			sleepRepository.delete(sleep);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == PLAYING) {
// 			Optional<Playing> optionalPlaying = playingRepository.findById(scheduleId);
// 			if (optionalPlaying.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Playing playing = optionalPlaying.get();
// 			long familyIdOfWriter = playing.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			playingRepository.delete(playing);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == TRAINING) {
// 			Optional<Training> optionalTraining = trainingRepository.findById(scheduleId);
// 			if (optionalTraining.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Training training = optionalTraining.get();
// 			long familyIdOfWriter = training.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			trainingRepository.delete(training);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == MENSTRUATION) {
// 			Optional<Menstruation> optionalMenstruation = menstruationRepository.findById(scheduleId);
// 			if (optionalMenstruation.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Menstruation menstruation = optionalMenstruation.get();
// 			long familyIdOfWriter = menstruation.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			menstruationRepository.delete(menstruation);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == WALK) {
// 			Optional<Walk> optionalWalk = walkRepository.findById(scheduleId);
// 			if (optionalWalk.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Walk walk = optionalWalk.get();
// 			long familyIdOfWriter = walk.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			walkRepository.delete(walk);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
// 		if (iconId == ETC) {
// 			Optional<Etc> optionalEtc = etcRepository.findById(scheduleId);
// 			if (optionalEtc.isEmpty()) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.INVALID_ID, "유효하지 않은 scheduleId 입니다. scheduleId : " + scheduleId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			Etc etc = optionalEtc.get();
// 			long familyIdOfWriter = etc.getUser().getFamily().getId();
// 			if (familyIdOfWriter != familyIdOfModifier) {
// 				return new ResponseEntity(
// 					NoDataResponse.response(status.NO_PERMISSION, "삭제 권한이 없습니다. userId : " + userId),
// 					HttpStatus.BAD_REQUEST);
// 			}
// 			etcRepository.delete(etc);
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS_DELETE_SCHEDULE),
// 				HttpStatus.OK);
// 		}
//
// 		return new ResponseEntity(NoDataResponse.response(status.INVALID_ID, "유효하지 않은 iconId 입니다. iconId : " + iconId),
// 			HttpStatus.OK);
// 	}
//
// 	/**
// 	 * W5 : 일정 조회 - 날짜 받아 시간순으로 정보 및 iconId, scheduleId 반환
// 	 */
// 	public ResponseEntity<String> getSchedule(ScheduleListDto scheduleListDto) {
// 		if (scheduleListDto.getUserId() == null) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.EMPTY_VALUE, "userId가 입력되지 않았습니다."),
// 				HttpStatus.BAD_REQUEST);
// 		}
// 		Optional<User> optionalUser = userRepository.findById(scheduleListDto.getUserId());
// 		if (optionalUser.isEmpty()) {
// 			return new ResponseEntity(
// 				NoDataResponse.response(status.INVALID_ID,
// 					"유효하지 않은 userId 입니다. userId : " + scheduleListDto.getUserId()),
// 				HttpStatus.BAD_REQUEST);
// 		}
// 		User user = optionalUser.get();
//
// 		List<Object> schedules = new ArrayList<>();
//
// 		for (long i = 1; i < (ETC + 1); i++) {
// 			if (i == FEEDING) {
// 				List<Feeding> feedings = feedingRepository.findFeedingsByUserAndRegisteredAt(user,
// 					scheduleListDto.dateToLocalDateTime());
// 				if(!feedings.isEmpty()) {
// 					schedules.addAll(feedings);
// 				}
// 				continue;
// 			}
// 			// if (i == SNACK) {
// 			// 	List<Snack> snacks = snackRepository.findFeedingsByUserAndRegisteredAt(user,
// 			// 		scheduleListDto.dateToLocalDateTime());
// 			// 	if(!feedings.isEmpty()) {
// 			// 		schedules.addAll(feedings);
// 			// 	}
// 			// 	continue;
// 			// 	return snackRepository;
// 			// }
// 			// if (i == POTTY) {
// 			// 	return pottyRepository;
// 			// }
// 			// if (i == DRUG) {
// 			// 	return drugRepository;
// 			// }
// 			// if (i == HOSPITAL) {
// 			// 	return hospitalRepository;
// 			// }
// 			// if (i == SALON) {
// 			// 	return salonRepository;
// 			// }
// 			// if (i == BATH) {
// 			// 	return bathRepository;
// 			// }
// 			// if (i == SLEEP) {
// 			// 	return sleepRepository;
// 			// }
// 			// if (i == PLAYING) {
// 			// 	return playingRepository;
// 			// }
// 			// if (i == TRAINING) {
// 			// 	return trainingRepository;
// 			// }
// 			// if (i == MENSTRUATION) {
// 			// 	return menstruationRepository;
// 			// }
// 			// if (i == WALK) {
// 			// 	return walkRepository;
// 			// }
// 			// if (i == ETC) {
// 			// 	return etcRepository;
// 			// }
// 		}
//
// 		return new ResponseEntity(NoDataResponse.response(status.SUCCESS, responseMessage.SUCCESS),
// 			HttpStatus.OK);
// 	}
//
// }
