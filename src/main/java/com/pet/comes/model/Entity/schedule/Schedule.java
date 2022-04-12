package com.pet.comes.model.Entity.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.pet.comes.dto.Req.ScheduleDto;
import com.pet.comes.model.Entity.Dog;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "d_type")
@DiscriminatorValue(value = "ETC")
public class Schedule extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "dog_id")
	private Dog dog;

	private LocalDate date;

	private LocalTime time;

	private String memo;

	/** 양방향매핑 */
	@OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
	private List<EtcItem> etcItems = new ArrayList<>();

	/** setter */
	protected void setUser(User user) {
		this.user = user;
	}

	protected void setDog(Dog dog) {
		this.dog = dog;
	}

	protected void setDate(String date) {
		this.date = LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE);
	}

	protected void setTime(String time) {
		this.time = LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME);
	}

	protected void setMemo(String memo) {
		this.memo = memo;
	}

	/** 생성자 */
	public Schedule(User user, ScheduleDto scheduleDto) {
		this.user = user;
		this.date = LocalDate.parse(scheduleDto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
		this.time = LocalTime.parse(scheduleDto.getTime(), DateTimeFormatter.ISO_LOCAL_TIME);
		this.memo = scheduleDto.getMemo();
	}

	public Schedule(User user, Dog dog, ScheduleDto scheduleDto) {
		this.user = user;
		this.dog = dog;
		this.date = LocalDate.parse(scheduleDto.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
		this.time = LocalTime.parse(scheduleDto.getTime(), DateTimeFormatter.ISO_LOCAL_TIME);
		this.memo = scheduleDto.getMemo();
	}

	/** update method */
	public void changeCommonItems(User user, Dog dog, String date, String time, String memo) {
		if(!user.equals(this.user)) {
			this.setUser(user);
		}
		if(!dog.equals(this.dog)) {
			this.setDog(dog);
		}
		if(!date.equals(String.valueOf(this.date))) {
			this.setDate(date);
		}
		if(!time.equals(String.valueOf(this.time))) {
			this.setTime(time);
		}
		if((memo != null) && (!memo.equals(this.memo))) {
			this.setMemo(memo);
		}
	}
}
