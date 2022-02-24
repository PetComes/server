package com.pet.comes.model.Entity.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import com.pet.comes.model.EnumType.DryOrWetFeed;
import com.pet.comes.model.EnumType.KindOfPotty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "potty")
@EntityListeners(AuditingEntityListener.class)
public class Potty {

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

	@Enumerated(EnumType.STRING)
	private KindOfPotty kind; // URINE(소변), FECES(대변)

	@LastModifiedDate
	private LocalDateTime modifiedAt;
	@CreatedDate
	private LocalDateTime registeredAt;

	public Potty(Map<String, String> pottyMap, User user) {
		this.user = user;
		this.date = LocalDate.parse(pottyMap.get("date"), DateTimeFormatter.ISO_DATE);
		this.time = LocalTime.parse(pottyMap.get("time"));
		this.memo = pottyMap.get("memo");
		this.kind = KindOfPotty.valueOf(pottyMap.get("kind"));
	}
}
