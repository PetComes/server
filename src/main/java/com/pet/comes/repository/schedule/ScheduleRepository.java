package com.pet.comes.repository.schedule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pet.comes.dto.Rep.ScheduleDto;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.Entity.schedule.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
	List<Schedule> findAllByUserAndDateBetweenOrderByDateAscTimeAsc(User user, LocalDate start, LocalDate end);
	List<Schedule> findAllByUserAndDateOrderByTimeAsc(User user, LocalDate date);

	@Query("SELECT NEW com.pet.comes.dto.Rep.ScheduleDto(s.id, s.user.id, s.dog.id, s.date, s.time, s.memo, type(s)) "
		 + "FROM Schedule AS s "
		 + "INNER JOIN s.user AS u "
		 + "LEFT JOIN u.family AS f "
		 + "WHERE f.id = u.family.id AND s.user = :user AND s.date BETWEEN :start AND :end")
	List<ScheduleDto> findAllFamilyMonthlyByUserAndDate(User user, LocalDate start, LocalDate end);
}
