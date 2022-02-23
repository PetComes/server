package com.pet.comes.model.Entity.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pet.comes.model.Entity.User;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "snack")
public class Snack {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private LocalDate date; // YYYY-MM-DD
	private LocalTime time; // HH:mm:ss

	@Column(columnDefinition = "TEXT")
	private String memo;    // 자유메모

	private String kind; 	// 간식종류 VARCHAR(255)

	private LocalDateTime modifiedAt;
	private LocalDateTime registeredAt;
}
