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

import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.User;

@Entity
@Table(name = "hospital")
public class Hospital {

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

	private String disease; // 질병 VARCHAR(255)

	@Column(columnDefinition = "TEXT")
	private String kind; // 시술/수술 종류 TEXT

	@Column(columnDefinition = "TEXT")
	private String prescriptionUrl; // 처방전 URL
	private int expenses;

	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address address;

	private Double weight; // 테이블에도 데이터 넣고, DogLog 에도 남기기 : 비즈니스로직에 반영

	private LocalDateTime modifiedAt;
	private LocalDateTime registeredAt;
}
