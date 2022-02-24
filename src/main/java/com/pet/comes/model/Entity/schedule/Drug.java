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

import com.pet.comes.model.Entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "drug")
@EntityListeners(AuditingEntityListener.class)
public class Drug {

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

	private String kind; // 약 종류 VARCHAR(255)

	@Column(columnDefinition = "TEXT")
	private String prescriptionUrl; // 처방전 URL
	private int expenses;

	@LastModifiedDate
	private LocalDateTime modifiedAt;
	@CreatedDate
	private LocalDateTime registeredAt;

	public Drug(Map<String, String> drugMap, User user) {
		this.user = user;
		this.date = LocalDate.parse(drugMap.get("date"), DateTimeFormatter.ISO_DATE);
		this.time = LocalTime.parse(drugMap.get("time"));
		this.memo = drugMap.get("memo");
		this.kind = drugMap.get("kind");
		this.prescriptionUrl = drugMap.get("prescriptionUrl");
		this.expenses = Integer.parseInt(drugMap.get("expenses"));
	}
}
