package com.pet.comes.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.comes.dto.Rep.DetailScheduleDto;
import com.pet.comes.dto.Rep.ScheduleDto;
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
	 * W1 ?????? ??????
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

		NoSuchElementException noSuchElementException = new NoSuchElementException("iconId ?????? ???????????? ???????????????.");
		log.error("ScheduleService - registerSchedule : iconId ?????? 0?????????.", noSuchElementException);
		throw noSuchElementException;
	}

	public User getValidUser(long userId) {
		validateUser(userId);
		return getUser(userId);
	}

	public void validateUser(long userId) {
		if (userId == 0) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException("userId ?????? 0?????????.");
			log.error("ScheduleService - validateUser : userId ?????? 0?????????.", illegalArgumentException);
			throw illegalArgumentException;
		}
	}

	public User getUser(long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ?????? userId ????????????. (userId : " + userId + ")");
			log.error(
				"SchedulerService - validateUser - optionalUser is Empty : ????????? ????????? ????????????. (userId : " + userId + ")",
				illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalUser.get();
	}

	public void validateDate(String date) {
		if (!date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException("????????? date ???????????????.");
			log.error("SchedulerService - validateDate : ????????? date ???????????????.", illegalArgumentException);
			throw illegalArgumentException;
		}
	}

	public void validateTime(String time) {
		if (!time.matches("^\\d{2}:([0-5][0-9]):(00)$")) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException("????????? time ???????????????.");
			log.error("SchedulerService - validateTime : ????????? time ???????????????.", illegalArgumentException);
			throw illegalArgumentException;
		}
	}

	public void validateDog(Dog dog) {
		if (dog == null) {
			IllegalStateException illegalStateException = new IllegalStateException(
				"?????? ????????? ???????????? ??????????????? ????????? id??? ??????????????? ?????????. (dogId : 0)");
			log.error("ScheduleService - validateDog : dog??? null?????????.", illegalStateException);
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
				"????????? weight ????????????. (weight : " + weight + ")");
			log.error("SchedulerService - logDogWeight : ????????? weight ????????????. (weight : " + weight + ")",
				illegalArgumentException);
			throw illegalArgumentException;
		}
	}

	public Dog getValidateDog(long dogId, User user) {
		Optional<Dog> optionalDog = dogRepository.findById(dogId);
		if (optionalDog.isEmpty()) {
			NoSuchElementException noSuchElementException = new NoSuchElementException(
				"???????????? ?????? dogId?????????. (dogId : " + dogId + ")");
			log.error("ScheduleService - getValidateDog : ???????????? ?????? dogId?????????. (dogId : " + dogId + ")",
				noSuchElementException);
			throw noSuchElementException;
		}

		Dog dog = optionalDog.get();
		Long dogFamilyId = dog.getFamily().getId();
		Long userFamilyId = user.getFamily().getId();

		if (!dogFamilyId.equals(userFamilyId)) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ???????????? ?????? ??????????????????.(dogId : " + dogId + ")");
			log.error("ScheduleService - getValidateDog : ???????????? ???????????? ?????? dogId?????????. (dogId : " + dogId + ")",
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
		if ((scheduleReqDto.getAddress() == null) && (scheduleReqDto.getX() == null) && (scheduleReqDto.getY() == null)
			&& (
			scheduleReqDto.getLocationName() == null)) {
			return null;
		} else {
			Optional<Address> address = addressRepository.findByLocationName(scheduleReqDto.getLocationName());
			return address.orElseGet(() -> new Address(scheduleReqDto));
		}
	}

	/**
	 * W3 ?????? ??????
	 * */
	@Transactional
	public void modifySchedule(ScheduleReqDto scheduleReqDto) {
		int iconId = scheduleReqDto.getIconId();
		if (iconId == 0) {
			NoSuchElementException noSuchElementException = new NoSuchElementException("iconId ?????? ???????????? ???????????????.");
			log.error("ScheduleService - modifySchedule : iconId ?????? 0?????????.", noSuchElementException);
			throw noSuchElementException;
		}

		long scheduleId = scheduleReqDto.getScheduleId();
		User user = getValidUser(scheduleReqDto.getUserId());
		long userFamilyId = user.getFamily().getId();

		Dog dog = null;
		if (scheduleReqDto.getDogId() != 0) {
			dog = getValidateDog(scheduleReqDto.getDogId(), user);
		}

		/* ???????????? ?????? */
		Optional<Schedule> optionalSchedule = scheduleRepository.findById(scheduleId);
		Schedule schedule = validateSchedule(optionalSchedule, scheduleId);

		long scheduleUserFamilyId = schedule.getUser().getFamily().getId();
		if (scheduleUserFamilyId != userFamilyId) {
			IllegalStateException illegalStateException = new IllegalStateException(
				"?????? ????????? ????????????. (userId : " + scheduleReqDto.getUserId() + ")");
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
				"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - modifySchedule - IconId.FEED : ???????????? ?????? scheduleId?????????. (scheduleId : "
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
			if ((scheduleReqDto.getKind() != null) || (scheduleReqDto.getType() != null) || (scheduleReqDto.getAmount()
				!= null))
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
				(scheduleReqDto.getX() != null) || (scheduleReqDto.getY() != null) || (scheduleReqDto.getLocationName()
				!= null))
				return true;
		}
		if (iconId == IconId.SALON) {
			if ((scheduleReqDto.getExpenses() != 0) || (scheduleReqDto.getDisease() != null) ||
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
				"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateFeed : ???????????? ?????? scheduleId?????????. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalFeed.get();
	}

	public Snack validateSnack(Optional<Snack> optionalSnack, long scheduleId) {
		if (optionalSnack.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateFeed : ???????????? ?????? scheduleId?????????. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalSnack.get();
	}

	public Potty validatePotty(Optional<Potty> optionalPotty, long scheduleId) {
		if (optionalPotty.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validatePotty : ???????????? ?????? scheduleId?????????. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalPotty.get();
	}

	public Drug validateDrug(Optional<Drug> optionalDrug, long scheduleId) {
		if (optionalDrug.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateDrug : ???????????? ?????? scheduleId?????????. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalDrug.get();
	}

	public Hospital validateHospital(Optional<Hospital> optionalHospital, long scheduleId) {
		if (optionalHospital.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateHospital : ???????????? ?????? scheduleId?????????. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalHospital.get();
	}

	public Salon validateSalon(Optional<Salon> optionalSalon, long scheduleId) {
		if (optionalSalon.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateSalon : ???????????? ?????? scheduleId?????????. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalSalon.get();
	}

	public Walk validateWalk(Optional<Walk> optionalWalk, long scheduleId) {
		if (optionalWalk.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
			log.error("SchedulerService - validateWalk : ???????????? ?????? scheduleId?????????. (scheduleId : "
				+ scheduleId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalWalk.get();
	}

	public EtcItem validateEtcItem(Optional<EtcItem> optionalEtcItem, String item) {
		if (optionalEtcItem.isEmpty()) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"???????????? ?????? item?????????. (item : " + item + ")");
			log.error("SchedulerService - validateEtcItem : ???????????? ?????? item?????????. (item : "
				+ item + ")", illegalArgumentException);
			throw illegalArgumentException;
		}
		return optionalEtcItem.get();
	}

	/**
	 * W4 ?????? ??????
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
				"?????? ????????? ????????????. (userId : " + scheduleReqDto.getUserId() + ")");
			log.error("ScheduleService - deleteSchedule : NOT SAME the familyId", illegalStateException);
			throw illegalStateException;
		}

		scheduleRepository.delete(schedule);
	}

	/**
	 * W5 ?????? ??????
	 */
	public List<ScheduleDto> getSchedules(Long userId, char type, String date) {
		User user = getValidUser(userId);
		LocalDate start = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);

		List<ScheduleDto> schedules;
		if (type == 'M') {
			LocalDate end = start.plusMonths(1);
			return scheduleRepository.findAllMonthlyByFamilyAndDateBetween(user.getFamily(), start, end);
		}

		schedules = scheduleRepository.findAllDailyByFamilyAndDate(user.getFamily(), start);
		return schedules;
	}

	/**
	 * W6 ?????? ?????? ??????
	 * */
	@Transactional
	public DetailScheduleDto getOneSchedule(Long scheduleId, Long userId) {
		User user = getUser(userId);
		Schedule schedule = getValidSchedule(scheduleId);

		if(checkAuthorityToGetSchedule(user, schedule)) {
			IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
				"?????? ?????? ??????????????????. (scheduleId : " + scheduleId + ", userId : " + userId + ")");
			log.error("SchedulerService - getOneSchedule : ?????? ???????????? ?????? ????????? ?????? ??????????????????. (scheduleId : " + scheduleId
				+ ", userId : " + userId + ")", illegalArgumentException);
			throw illegalArgumentException;
		}

		return DetailScheduleDto.convertToDetailScheduleDto(schedule); // schedule;
	}

	public Schedule getValidSchedule(Long scheduleId) {
		if (scheduleRepository.existsById(scheduleId)) {
			return scheduleRepository.findById(scheduleId).get();
		}
		IllegalArgumentException illegalArgumentException = new IllegalArgumentException(
			"???????????? ?????? scheduleId?????????. (scheduleId : " + scheduleId + ")");
		log.error("SchedulerService - modifySchedule - IconId.FEED : ???????????? ?????? scheduleId?????????. (scheduleId : "
			+ scheduleId + ")", illegalArgumentException);
		throw illegalArgumentException;
	}

	public boolean checkAuthorityToGetSchedule(User user, Schedule schedule) {
		return schedule.getUser().getId().equals(user.getId()) &&
			user.getFamily()
				.getUsers()
				.stream()
				.noneMatch(x -> x.getId().equals(schedule.getUser().getId()));
	}
}
