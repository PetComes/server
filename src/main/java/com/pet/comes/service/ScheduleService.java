package com.pet.comes.service;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.dto.etc.IconId;
import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.DogLog;
import com.pet.comes.model.Entity.Family;
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
	public long registerSchedule(ScheduleDto scheduleDto) {
		int iconId = scheduleDto.getIconId();
		User user = getValidateUser(scheduleDto.getUserId());
		Dog dog = null;

		validateDate(scheduleDto.getDate());
		validateTime(scheduleDto.getTime());

		if (scheduleDto.getDogId() != 0) {
			dog = getValidateDog(scheduleDto.getDogId(), user);
		}

		if (iconId == IconId.FEED) {
			Feed feed = new Feed(user, dog, scheduleDto);
			feedRepository.save(feed);
			return feed.getId();
		}

		if (iconId == IconId.SNACK) {
			Snack snack = new Snack(user, dog, scheduleDto);
			snackRepository.save(snack);
			return snack.getId();
		}

		if (iconId == IconId.POTTY) {
			Potty potty = new Potty(user, dog, scheduleDto);
			pottyRepository.save(potty);
			return potty.getId();
		}

		if (iconId == IconId.DRUG) {
			Drug drug = new Drug(user, dog, scheduleDto);
			drugRepository.save(drug);
			return drug.getId();
		}

		if (iconId == IconId.HOSPITAL) {
			if (scheduleDto.getWeight() != 0.0) {
				validateDog(dog);
				writeDogWeightLog(dog, scheduleDto.getWeight());
			}
			Address address = getValidAddress(scheduleDto);
			Hospital hospital = new Hospital(user, dog, address, scheduleDto);
			hospitalRepository.save(hospital);
			return hospital.getId();
		}

		if (iconId == IconId.SALON) {
			Address address = getValidAddress(scheduleDto);
			Salon salon = new Salon(user, dog, address, scheduleDto);
			salonRepository.save(salon);
			return salon.getId();
		}

		if (iconId == IconId.BATH) {
			Bath bath = new Bath(user, dog, scheduleDto);
			bathRepository.save(bath);
			return bath.getId();
		}

		if (iconId == IconId.SLEEP) {
			Sleep sleep = new Sleep(user, dog, scheduleDto);
			sleepRepository.save(sleep);
			return sleep.getId();
		}

		if (iconId == IconId.PLAY) {
			Play play = new Play(user, dog, scheduleDto);
			playRepository.save(play);
			return play.getId();
		}

		if (iconId == IconId.TRAINING) {
			Training training = new Training(user, dog, scheduleDto);
			trainingRepository.save(training);
			return training.getId();
		}

		if (iconId == IconId.MENSTRUATION) {
			Menstruation menstruation = new Menstruation(user, dog, scheduleDto);
			menstruationRepository.save(menstruation);
			return menstruation.getId();
		}

		if (iconId == IconId.WALK) {
			Walk walk = new Walk(user, dog, scheduleDto);
			walkRepository.save(walk);
			return walk.getId();
		}

		if (iconId == IconId.ETC) {
			Map<String, String> etcItems = scheduleDto.getEtcMap();
			int numberOfItems = scheduleDto.getAdditionalItemNo();
			Schedule etc = new Schedule(user, dog, scheduleDto);
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

	public User getValidateUser(long userId) {
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

	public Address getValidAddress(ScheduleDto scheduleDto) {
		if (scheduleDto.getAddress() == null && scheduleDto.getX() == null && scheduleDto.getY() == null
			&& scheduleDto.getLocationName() == null) {
			return null;
		}

		Optional<Address> optionalAddress = addressRepository.findByLocationName(scheduleDto.getLocationName());
		if (optionalAddress.isPresent()) {
			addressRepository.save(optionalAddress.get());
			return optionalAddress.get();
		}

		return new Address(scheduleDto);
	}

	/**
	 * W2 일정 수정
	 * */
	@Transactional
	public void modifySchedule(ScheduleDto scheduleDto) {

	}

}
