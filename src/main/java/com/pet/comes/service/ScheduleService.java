package com.pet.comes.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.comes.dto.Rep.ScheduleDto;
import com.pet.comes.dto.Req.ScheduleConditionDto;
import com.pet.comes.dto.Req.ScheduleReqDto;
import com.pet.comes.dto.etc.IconId;
import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.DogLog;
import com.pet.comes.model.Entity.User;
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
import com.pet.comes.repository.AddressRepository;
import com.pet.comes.repository.DogLogRepository;
import com.pet.comes.repository.DogRepository;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.repository.schedule.BathRepository;
import com.pet.comes.repository.schedule.DrugRepository;
import com.pet.comes.repository.schedule.EtcItemRepository;
import com.pet.comes.repository.schedule.FeedRepository;
import com.pet.comes.repository.schedule.HospitalRepository;
import com.pet.comes.repository.schedule.MenstruationRepository;
import com.pet.comes.repository.schedule.PlayRepository;
import com.pet.comes.repository.schedule.PottyRepository;
import com.pet.comes.repository.schedule.SalonRepository;
import com.pet.comes.repository.schedule.ScheduleRepository;
import com.pet.comes.repository.schedule.SleepRepository;
import com.pet.comes.repository.schedule.SnackRepository;
import com.pet.comes.repository.schedule.TrainingRepository;
import com.pet.comes.repository.schedule.WalkRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final UserRepository userRepository;
	private final DogRepository dogRepository;
	private final DogLogRepository dogLogRepository;
	private final AddressRepository addressRepository;

	private final FeedRepository feedRepository;
	private final SnackRepository snackRepository;
	private final PottyRepository pottyRepository;
	private final DrugRepository drugRepository;
	private final HospitalRepository hospitalRepository;
	private final SalonRepository salonRepository;
	private final BathRepository bathRepository;
	private final SleepRepository sleepRepository;
	private final PlayRepository playRepository;
	private final TrainingRepository trainingRepository;
	private final MenstruationRepository menstruationRepository;
	private final WalkRepository walkRepository;
	private final ScheduleRepository scheduleRepository;
	private final EtcItemRepository etcItemRepository;

	/**
	 * W1 일정 등록
	 * */
	@Transactional
	public long registerSchedule(ScheduleReqDto scheduleReqDto) {
		int iconId = scheduleReqDto.getIconId();
		User user = getValidUser(scheduleReqDto.getUserId());
		Dog dog = null;

		validateDate(scheduleReqDto.getDate());
		validateTime(scheduleReqDto.getTime());

		if (scheduleReqDto.getDogId() != 0) {
			dog = getValidateDog(scheduleReqDto.getDogId(), user);
		}

		if (iconId == IconId.FEED) {
			Feed feed = new Feed(user, dog, scheduleReqDto);
			feedRepository.save(feed);
			return feed.getId();
		}

		if (iconId == IconId.SNACK) {
			Snack snack = new Snack(user, dog, scheduleReqDto);
			snackRepository.save(snack);
			return snack.getId();
		}

		if (iconId == IconId.POTTY) {
			Potty potty = new Potty(user, dog, scheduleReqDto);
			pottyRepository.save(potty);
			return potty.getId();
		}

		if (iconId == IconId.DRUG) {
			Drug drug = new Drug(user, dog, scheduleReqDto);
			drugRepository.save(drug);
			return drug.getId();
		}

		if (iconId == IconId.HOSPITAL) {
			Address address = getValidAddress(scheduleReqDto);
			Hospital hospital = new Hospital(user, dog, address, scheduleReqDto);
			if (scheduleReqDto.getWeight() != 0.0) {
				validateDog(dog);
				writeDogWeightLog(dog, scheduleReqDto.getWeight());
				hospital.modifyWeight(scheduleReqDto.getWeight());
			}

			hospitalRepository.save(hospital);
			return hospital.getId();
		}

		if (iconId == IconId.SALON) {
			Address address = getValidAddress(scheduleReqDto);
			Salon salon = new Salon(user, dog, address, scheduleReqDto);
			salonRepository.save(salon);
			return salon.getId();
		}

		if (iconId == IconId.BATH) {
			Bath bath = new Bath(user, dog, scheduleReqDto);
			bathRepository.save(bath);
			return bath.getId();
		}

		if (iconId == IconId.SLEEP) {
			Sleep sleep = new Sleep(user, dog, scheduleReqDto);
			sleepRepository.save(sleep);
			return sleep.getId();
		}

		if (iconId == IconId.PLAY) {
			Play play = new Play(user, dog, scheduleReqDto);
			playRepository.save(play);
			return play.getId();
		}

		if (iconId == IconId.TRAINING) {
			Training training = new Training(user, dog, scheduleReqDto);
			trainingRepository.save(training);
			return training.getId();
		}

		if (iconId == IconId.MENSTRUATION) {
			Menstruation menstruation = new Menstruation(user, dog, scheduleReqDto);
			menstruationRepository.save(menstruation);
			return menstruation.getId();
		}

		if (iconId == IconId.WALK) {
			Walk walk = new Walk(user, dog, scheduleReqDto);
			walkRepository.save(walk);
			return walk.getId();
		}

		if (iconId == IconId.ETC) {
			Map<String, String> etcItems = scheduleReqDto.getEtcMap();
			int numberOfItems = scheduleReqDto.getAdditionalItemNo();
			Schedule etc = new Schedule(user, dog, scheduleReqDto);
			scheduleRepository.save(etc);
			EtcItem etcItem;
			String item, value;
			for (int i = 0; i < numberOfItems; i++) {
				item = etcItems.get("item" + i);
				value = etcItems.get("value" + i);
				etcItem = new EtcItem(user, item, value, etc);
				etcItemRepository.save(etcItem);
			}
			return etc.getId();
		}

		NoSuchElementException noSuchElementException = new NoSuchElementException("iconId 값이 입력되지 않았습니다.");
		log.error("ScheduleService - registerSchedule : iconId 값이 0입니다.", noSuchElementException);
		throw noSuchElementException;
	}

	public User getValidUser(long userId) {
		validateUser(userId);
		return getUser(userId);
	}

	public void validateUser(long userId) {
		if (userId == 0) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException("userId 값이 0입니다.");
			log.error("ScheduleService - validateUser : userId 값이 0입니다.", illegalArgumentException);
			throw illegalArgumentException;
		}
	}

	public User getUser(long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 userId 값입니다. (userId : " + userId + ")");
			log.error(
				"SchedulerService - validateUser - optionalUser is Empty : 유효한 유저가 아닙니다. (userId : " + userId + ")",
				illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalUser.get();
	}

	public void validateDate(String date) {
		if (!date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException("잘못된 date 형식입니다.");
			log.error("SchedulerService - validateDate : 잘못된 date 형식입니다.", illegalArgumentException);
			throw illegalArgumentException;
		}
	}

	public void validateTime(String time) {
		if (!time.matches("^\\d{2}:([0-5][0-9]):(00)$")) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException("잘못된 time 형식입니다.");
			log.error("SchedulerService - validateTime : 잘못된 time 형식입니다.", illegalArgumentException);
			throw illegalArgumentException;
		}
	}

	public void validateDog(Dog dog) {
		if (dog == null) {
			IllegalStateException illegalStateException = new IllegalStateException(
				"병원 일정에 몸무게를 등록하려면 강아지 id가 입력되어야 합니다. (dogId : 0)");
			log.error("ScheduleService - validateDog : dog이 null입니다.", illegalStateException);
			throw illegalStateException;
		}
	}

	public void writeDogWeightLog(Dog dog, double afterWeight) {
		validateWeight(afterWeight);
		float beforeWeight = dog.getWeight();
		if (beforeWeight != afterWeight) {
			saveDogWeightLog(dog, (float)afterWeight);
			updateDogWeight(dog, (float)afterWeight);
		}
	}

	public void validateWeight(double weight) {
		if (weight < 0.0) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"잘못된 weight 값입니다. (weight : " + weight + ")");
			log.error("SchedulerService - logDogWeight : 잘못된 weight 값입니다. (weight : " + weight + ")",
				illegalArgumentException);
			throw illegalArgumentException;
		}
	}

	public Dog getValidateDog(long dogId, User user) {
		Optional<Dog> optionalDog = dogRepository.findById(dogId);
		if (optionalDog.isEmpty()) {
			NoSuchElementException noSuchElementException = new NoSuchElementException(
				"유효하지 않은 dogId입니다. (dogId : " + dogId + ")");
			log.error("ScheduleService - getValidateDog : 유효하지 않은 dogId입니다. (dogId : " + dogId + ")",
				noSuchElementException);
			throw noSuchElementException;
		}

		Dog dog = optionalDog.get();
		Long dogFamilyId = dog.getFamily().getId();
		Long userFamilyId = user.getFamily().getId();

		if (!dogFamilyId.equals(userFamilyId)) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"가족으로 등록되지 않은 강아지입니다.(dogId : " + dogId + ")");
			log.error("ScheduleService - getValidateDog : 가족으로 등록되지 않은 dogId입니다. (dogId : " + dogId + ")",
				illegalArgumentException);
			throw illegalArgumentException;
		}
		return dog;
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

	public Address getValidAddress(ScheduleReqDto scheduleReqDto) {
		if ((scheduleReqDto.getAddress() == null) && (scheduleReqDto.getX() == null) && (scheduleReqDto.getY() == null) && (
			scheduleReqDto.getLocationName() == null)) {
			return null;
		} else {
			Optional<Address> address = addressRepository.findByLocationName(scheduleReqDto.getLocationName());
			return address.orElseGet(() -> new Address(scheduleReqDto));
		}
	}

	/**
	 * W3 일정 수정
	 * */
	@Transactional
	public void modifySchedule(ScheduleReqDto scheduleReqDto) {
		int iconId = scheduleReqDto.getIconId();
		if (iconId == 0) {
			NoSuchElementException noSuchElementException = new NoSuchElementException("iconId 값이 입력되지 않았습니다.");
			log.error("ScheduleService - modifySchedule : iconId 값이 0입니다.", noSuchElementException);
			throw noSuchElementException;
		}

		long scheduleId = scheduleReqDto.getScheduleId();
		User user = getValidUser(scheduleReqDto.getUserId());
		long userFamilyId = user.getFamily().getId();

		Dog dog = null;
		if (scheduleReqDto.getDogId() != 0) {
			dog = getValidateDog(scheduleReqDto.getDogId(), user);
		}

		/* 공통사항 수정 */
		Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
		Schedule schedule = validateSchedule(optionalSchedule, scheduleId);

		long scheduleUserFamilyId = schedule.getUser().getFamily().getId();
		if (scheduleUserFamilyId != userFamilyId) {
			IllegalStateException illegalStateException = new IllegalStateException(
				"수정 권한이 없습니다. (userId : " + scheduleReqDto.getUserId() + ")");
			log.error("ScheduleService - modifySchedule : NOT SAME the familyId", illegalStateException);
			throw illegalStateException;
		}

		modifyCommonItems(schedule, user, dog, scheduleReqDto);
		scheduleRepository.save(schedule);

		if ((iconId == IconId.FEED) && (isExistItemsToModified(iconId, scheduleReqDto))) {
			Optional<Feed> optionalFeed = feedRepository.findById(scheduleId);

			Feed feed = validateFeed(optionalFeed, scheduleId);
			feed.modifyKind(scheduleReqDto.getKind());
			feed.modifyType(scheduleReqDto.getType());
			feed.modifyAmount(scheduleReqDto.getAmount());

			feedRepository.save(feed);
		}

		if (iconId == IconId.SNACK && (isExistItemsToModified(iconId, scheduleReqDto))) {
			Optional<Snack> optionalSnack = snackRepository.findById(scheduleId);
			Snack snack = validateSnack(optionalSnack, scheduleId);
			snack.modifyKind(scheduleReqDto.getKind());
			snackRepository.save(snack);
		}

		if (iconId == IconId.POTTY && (isExistItemsToModified(iconId, scheduleReqDto))) {
			Optional<Potty> optionalPotty = pottyRepository.findById(scheduleId);
			Potty potty = validatePotty(optionalPotty, scheduleId);
			potty.modifyKind(scheduleReqDto.getKind());
			pottyRepository.save(potty);
		}

		if (iconId == IconId.DRUG && (isExistItemsToModified(iconId, scheduleReqDto))) {
			Optional<Drug> optionalDrug = drugRepository.findById(scheduleId);

			Drug drug = validateDrug(optionalDrug, scheduleId);
			drug.modifyKind(scheduleReqDto.getKind());
			drug.modifyPrescriptionUrl(scheduleReqDto.getPrescriptionUrl());
			drug.modifyExpenses(scheduleReqDto.getExpenses());

			drugRepository.save(drug);
		}

		if (iconId == IconId.HOSPITAL && (isExistItemsToModified(iconId, scheduleReqDto))) {
			Optional<Hospital> optionalHospital = hospitalRepository.findById(scheduleId);

			Hospital hospital = validateHospital(optionalHospital, scheduleId);
			hospital.modifyDisease(scheduleReqDto.getDisease());
			hospital.modifyKind(scheduleReqDto.getKind());
			hospital.modifyPrescriptionUrl(scheduleReqDto.getPrescriptionUrl());
			hospital.modifyExpenses(scheduleReqDto.getExpenses());

			Address address = getValidAddress(scheduleReqDto);
			if ((address != null) && ((address.getAddressId() == null))) {
				addressRepository.save(address);
			}
			hospital.modifyAddress(address);

			if (scheduleReqDto.getWeight() != 0.0) {
				validateDog(dog);
				writeDogWeightLog(dog, scheduleReqDto.getWeight());
				hospital.modifyWeight(scheduleReqDto.getWeight());
			}

			hospitalRepository.save(hospital);
		}

		if (iconId == IconId.SALON && (isExistItemsToModified(iconId, scheduleReqDto))) {
			Optional<Salon> optionalSalon = salonRepository.findById(scheduleId);
			Salon salon = validateSalon(optionalSalon, scheduleId);
			salon.modifyExpenses(scheduleReqDto.getExpenses());

			Address address = getValidAddress(scheduleReqDto);
			if ((address != null) && (address.getAddressId() == null)) {
				addressRepository.save(address);
			}
			salon.modifyAddress(address);

			salonRepository.save(salon);
		}

		if (iconId == IconId.WALK && (isExistItemsToModified(iconId, scheduleReqDto))) {
			Optional<Walk> optionalWalk = walkRepository.findById(scheduleId);
			Walk walk = validateWalk(optionalWalk, scheduleId);
			walk.modifyStartTime(scheduleReqDto.getStartTime());
			walk.modifyEndTime(scheduleReqDto.getEndTime());
			walkRepository.save(walk);
		}

		if (iconId == IconId.ETC && (isExistItemsToModified(iconId, scheduleReqDto))) {
			List<EtcItem> etcItems = schedule.getEtcItems();
			Map<String, String> itemMap = scheduleReqDto.getEtcMap();

			Optional<EtcItem> optionalEtcItem;
			EtcItem etcItem;
			int i = 0;
			for (String key : itemMap.keySet()) {
				optionalEtcItem = etcItems.stream().filter(x -> x.getKey().equals(itemMap.get(key))).findFirst();
				if (!key.contains("value")) {
					etcItem = validateEtcItem(optionalEtcItem, itemMap.get(key));
					etcItem.modifyValue(itemMap.get("value" + i++));
				}
			}

			scheduleRepository.save(schedule);
		}

	}

	public Schedule validateSchedule(Optional<Schedule> optionalSchedule, long scheduleId) {
		if (optionalSchedule.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 scheduleId입니다. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - modifySchedule - IconId.FEED : 유효하지 않은 scheduleId입니다. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalSchedule.get();
	}

	public void modifyCommonItems(Schedule schedule, User user, Dog dog, ScheduleReqDto scheduleReqDto) {
		User newUser = schedule.getUser();
		Dog newDog = schedule.getDog();
		String newDate = String.valueOf(schedule.getDate());
		String newTime = String.valueOf(schedule.getTime());
		String newMemo = schedule.getMemo();

		if (user != null) {
			newUser = user;
		}
		if (dog != null) {
			newDog = dog;
		}
		if (scheduleReqDto.getDate() != null) {
			validateDate(scheduleReqDto.getDate());
			newDate = scheduleReqDto.getDate();
		}
		if (scheduleReqDto.getTime() != null) {
			validateTime(scheduleReqDto.getTime());
			newTime = scheduleReqDto.getTime();
		}
		if (scheduleReqDto.getMemo() != null) {
			newMemo = scheduleReqDto.getMemo();
		}

		schedule.changeCommonItems(newUser, newDog, newDate, newTime, newMemo);
	}

	public boolean isExistItemsToModified(int iconId, ScheduleReqDto scheduleReqDto) {
		if (iconId == IconId.FEED) {
			if ((scheduleReqDto.getKind() != null) || (scheduleReqDto.getType() != null) || (scheduleReqDto.getAmount() != null))
				return true;
		}
		if (iconId == IconId.SNACK) {
			if (scheduleReqDto.getKind() != null)
				return true;
		}
		if (iconId == IconId.POTTY) {
			if (scheduleReqDto.getKind() != null)
				return true;
		}
		if (iconId == IconId.DRUG) {
			if ((scheduleReqDto.getKind() != null) || (scheduleReqDto.getPrescriptionUrl() != null) ||
				(scheduleReqDto.getExpenses() != 0))
				return true;
		}
		if (iconId == IconId.HOSPITAL) {
			if ((scheduleReqDto.getKind() != null) || (scheduleReqDto.getPrescriptionUrl() != null) ||
				(scheduleReqDto.getExpenses() != 0) || (scheduleReqDto.getWeight() != 0.0) ||
				(scheduleReqDto.getDisease() != null) || (scheduleReqDto.getAddress() != null) ||
				(scheduleReqDto.getX() != null) || (scheduleReqDto.getY() != null) || (scheduleReqDto.getLocationName() != null))
				return true;
		}
		if (iconId == IconId.SALON) {
			if((scheduleReqDto.getExpenses() != 0) || (scheduleReqDto.getDisease() != null) ||
				(scheduleReqDto.getAddress() != null) || (scheduleReqDto.getX() != null) ||
				(scheduleReqDto.getY() != null) || (scheduleReqDto.getLocationName() != null))
				return true;
		}
		if (iconId == IconId.WALK) {
			if ((scheduleReqDto.getStartTime() != null) || (scheduleReqDto.getEndTime() != null))
				return true;
		}
		if (iconId == IconId.ETC) {
			if (scheduleReqDto.getEtcMap() != null)
				return true;
		}

		return false;
	}

	public Feed validateFeed(Optional<Feed> optionalFeed, long scheduleId) {
		if (optionalFeed.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 scheduleId입니다. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateFeed : 유효하지 않은 scheduleId입니다. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalFeed.get();
	}

	public Snack validateSnack(Optional<Snack> optionalSnack, long scheduleId) {
		if (optionalSnack.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 scheduleId입니다. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateFeed : 유효하지 않은 scheduleId입니다. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalSnack.get();
	}

	public Potty validatePotty(Optional<Potty> optionalPotty, long scheduleId) {
		if (optionalPotty.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 scheduleId입니다. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validatePotty : 유효하지 않은 scheduleId입니다. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalPotty.get();
	}

	public Drug validateDrug(Optional<Drug> optionalDrug, long scheduleId) {
		if (optionalDrug.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 scheduleId입니다. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateDrug : 유효하지 않은 scheduleId입니다. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalDrug.get();
	}

	public Hospital validateHospital(Optional<Hospital> optionalHospital, long scheduleId) {
		if (optionalHospital.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 scheduleId입니다. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateHospital : 유효하지 않은 scheduleId입니다. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalHospital.get();
	}

	public Salon validateSalon(Optional<Salon> optionalSalon, long scheduleId) {
		if (optionalSalon.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 scheduleId입니다. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateSalon : 유효하지 않은 scheduleId입니다. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalSalon.get();
	}

	public Walk validateWalk(Optional<Walk> optionalWalk, long scheduleId) {
		if (optionalWalk.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 scheduleId입니다. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateWalk : 유효하지 않은 scheduleId입니다. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalWalk.get();
	}

	public EtcItem validateEtcItem(Optional<EtcItem> optionalEtcItem, String item) {
		if (optionalEtcItem.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"유효하지 않은 item입니다. (item : " + item + ")");
			log.error("SchedulerService - validateEtcItem : 유효하지 않은 item입니다. (item : "
				+ item + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalEtcItem.get();
	}

	/**
	 * W4 일정 삭제
	 **/
	public void deleteSchedule(ScheduleReqDto scheduleReqDto) {
		long scheduleId = scheduleReqDto.getScheduleId();
		User user = getValidUser(scheduleReqDto.getUserId());
		long userFamilyId = user.getFamily().getId();

		Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
		Schedule schedule = validateSchedule(optionalSchedule, scheduleId);

		long scheduleUserFamilyId = schedule.getUser().getFamily().getId();
		if (scheduleUserFamilyId != userFamilyId) {
			IllegalStateException illegalStateException = new IllegalStateException(
				"삭제 권한이 없습니다. (userId : " + scheduleReqDto.getUserId() + ")");
			log.error("ScheduleService - deleteSchedule : NOT SAME the familyId", illegalStateException);
			throw illegalStateException;
		}

		scheduleRepository.delete(schedule);
	}

	/**
	 * W5 일정 조회
	 */
	public List<ScheduleDto> getSchedules(ScheduleConditionDto scheduleConditionDto) {
		User user = getValidUser(scheduleConditionDto.getUserId());
		List<User> userFamily = user.getFamily().getUsers();

		LocalDate start = LocalDate.parse(scheduleConditionDto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate end = start.plusMonths(1);

		List<ScheduleDto> schedules;
		// Monthly
		schedules = scheduleRepository.findAllFamilyMonthlyByUserAndDate(user, start, end);
		// Daily

		// if(scheduleConditionDto.getFlag() == 'M') {
		// 	schedules = getMonthlySchedules(user, scheduleConditionDto.getDate());
		// }
		// else {
		// 	schedules = getDailySchedules(user, scheduleConditionDto.getDate());
		// }

		return schedules;
	}

	public List<Schedule> getMonthlySchedules(User user, String date) {
		LocalDate start = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
		LocalDate end = start.plusMonths(1);
		return scheduleRepository.findAllByUserAndDateBetweenOrderByDateAscTimeAsc(user, start, end);
	}

	public List<Schedule> getDailySchedules(User user, String date) {
		LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
		return scheduleRepository.findAllByUserAndDateOrderByTimeAsc(user, localDate);
	}
}
