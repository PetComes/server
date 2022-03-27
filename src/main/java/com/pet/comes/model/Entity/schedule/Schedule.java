package com.pet.comes.model.Entity.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.pet.comes.model.Entity.User;
import com.pet.comes.model.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "d_type")
public abstract class Schedule extends Timestamped {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	private LocalDate date;

	private LocalTime time;

	private String memo;

	/** setter */
	protected void setUser(User user) {
		this.user = user;
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
}
