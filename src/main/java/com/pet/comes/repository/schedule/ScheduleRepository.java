package com.pet.comes.repository.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pet.comes.dto.Rep.ScheduleDto;
import com.pet.comes.model.Entity.Family;
import com.pet.comes.model.Entity.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

	@Query("SELECT NEW com.pet.comes.dto.Rep.ScheduleDto(s.id, s.user.id, s.dog.id, s.date, s.time, s.memo, type(s)) "
		 + "FROM Schedule AS s "
		 + "INNER JOIN s.user AS u "
		 + "WHERE u.family = :family AND s.date BETWEEN :start AND :end "
		 + "ORDER BY type(s), s.date")
	List<ScheduleDto> findAllMonthlyByFamilyAndDateBetween(Family family, LocalDate start, LocalDate end);

	@Query("SELECT NEW com.pet.comes.dto.Rep.ScheduleDto(s.id, s.user.id, s.dog.id, s.date, s.time, s.memo, type(s)) "
		 + "FROM Schedule AS s "
		 + "INNER JOIN s.user AS u "
		 + "WHERE u.family = :family AND s.date = :date "
		 + "ORDER BY type(s), s.date")
	List<ScheduleDto> findAllDailyByFamilyAndDate(Family family, LocalDate date);
}
