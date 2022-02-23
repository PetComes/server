package com.pet.comes.model.Entity.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
	private String memo;    // 자유메모

	private String disease;
	private String kind;
	private String prescriptionUrl;
	private int expenses;

	@ManyToOne
	@JoinColumn(name = "address_id")
	private Address address;

	private Double weight; // 여기서 해줄지 DogLog API 처럼 할지 고민 아님 둘 다 할지?

	private LocalDateTime modifiedAt;
	private LocalDateTime registeredAt;
}
