package com.pet.comes.model.Entity.schedule;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.pet.comes.model.Entity.User;

@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Schedule {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private LocalDate date; 	   // YYYY-MM-DD
	private LocalTime time;		   // HH:mm:ss

	@Column(columnDefinition = "TEXT")
	private String memo;    	   // 자유메모
}
