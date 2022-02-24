package com.pet.comes.model.Entity.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.pet.comes.model.Entity.Address;
import com.pet.comes.model.Entity.User;
import com.pet.comes.model.EnumType.DryOrWetFeed;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "hospital")
@EntityListeners(AuditingEntityListener.class)
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

	private Float weight; // 테이블에도 데이터 넣고, DogLog 에도 남기기 : 비즈니스로직에 반영

	@LastModifiedDate
	private LocalDateTime modifiedAt;
	@CreatedDate
	private LocalDateTime registeredAt;

	public Hospital (Map<String, String> hospitalMap, User user, Address address) {
		this.user = user;
		this.date = LocalDate.parse(hospitalMap.get("date"), DateTimeFormatter.ISO_DATE);
		this.time = LocalTime.parse(hospitalMap.get("time"));
		this.memo = hospitalMap.get("memo");
		this.disease = hospitalMap.get("disease");
		this.kind = hospitalMap.get("kind");
		this.prescriptionUrl = hospitalMap.get("prescriptionUrl");
		this.expenses = Integer.parseInt(hospitalMap.get("expenses"));
		this.address = address;
		this.weight = Float.parseFloat(hospitalMap.get("weight"));
	}
}
