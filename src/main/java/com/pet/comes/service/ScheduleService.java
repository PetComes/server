package com.pet.comes.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.dto.etc.IconId;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.Entity.schedule.Drug;
import com.pet.comes.model.Entity.schedule.Feed;
import com.pet.comes.model.Entity.schedule.Potty;
import com.pet.comes.model.Entity.schedule.Schedule;
import com.pet.comes.model.Entity.schedule.Snack;
import com.pet.comes.repository.UserRepository;
import com.pet.comes.repository.schedule.DrugRepository;
import com.pet.comes.repository.schedule.FeedRepository;
import com.pet.comes.repository.schedule.PottyRepository;
import com.pet.comes.repository.schedule.SnackRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

	private final UserRepository userRepository;
	private final FeedRepository feedRepository;
	private final SnackRepository snackRepository;
	private final PottyRepository pottyRepository;
	private final DrugRepository drugRepository;

	/**
	 * W1 일정 등록
	 * */
	public long registerSchedule(ScheduleDto scheduleDto) {
		int iconId = scheduleDto.getIconId();
		User user = getValidateUser(scheduleDto.getUserId());

		validateDate(scheduleDto.getDate());
		validateTime(scheduleDto.getTime());

		if(iconId == IconId.FEED) {
			Feed feed = new Feed(user, scheduleDto);
			feedRepository.save(feed);
			return feed.getId();
		}

		if(iconId == IconId.SNACK) {
			Snack snack = new Snack(user, scheduleDto);
			snackRepository.save(snack);
			return snack.getId();
		}

		if(iconId == IconId.POTTY) {
			Potty potty = new Potty(user, scheduleDto);
			pottyRepository.save(potty);
			return potty.getId();
		}

		if(iconId == IconId.DRUG) {
			Drug drug = new Drug(user, scheduleDto);
			drugRepository.save(drug);
			return drug.getId();
		}

		return 0;
	}

	public User getValidateUser(long userId) {
		validateUser(userId);
		return getUser(userId);
	}

	public void validateUser(long userId) {
		if (userId == 0) {
			IllegalArgumentException exception = new IllegalArgumentException("userId 값이 0입니다.");
			log.error("SchedulerService - validateUser : userId 값이 0입니다.", exception);
			throw exception;
		}
	}

	public User getUser(long userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isEmpty()) {
			IllegalArgumentException exception = new IllegalArgumentException(
				"유효하지 않은 userId 값입니다. (userId : " + userId + ")");
			log.error(
				"SchedulerService - validateUser - optionalUser is Empty : 유효한 유저가 아닙니다. (userId : " + userId + ")",
				exception);
			throw exception;
		}
		return optionalUser.get();
	}

	public void validateDate(String date) {
		if(!date.matches("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$")) {
			IllegalArgumentException exception = new IllegalArgumentException("잘못된 date 형식입니다.");
			log.error("SchedulerService - validateDate : 잘못된 date 형식입니다.", exception);
			throw exception;
		}
	}

	public void validateTime(String time) {
		if(!time.matches("^\\d{2}:([0-5][0-9]):(00)$")) {
			IllegalArgumentException exception = new IllegalArgumentException("잘못된 time 형식입니다.");
			log.error("SchedulerService - validateTime : 잘못된 time 형식입니다.", exception);
			throw exception;
		}
	}

	public void registerFeed(User user, ScheduleDto scheduleDto) {
		Schedule schedule = new Feed(user, scheduleDto);

	}

}
