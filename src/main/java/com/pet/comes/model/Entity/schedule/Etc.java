package com.pet.comes.model.Entity.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "etc")
@EntityListeners(AuditingEntityListener.class)
public class Etc {

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

	@OneToMany(mappedBy = "etc")
	private List<AdditionalItem> additionalItemList = new ArrayList<>();

	@LastModifiedDate
	private LocalDateTime modifiedAt;
	@CreatedDate
	private LocalDateTime registeredAt;

	public void addAdditionalItem(AdditionalItem additionalItem) {
		if(additionalItem.getEtc() != this) {
			additionalItem.changeEtc(this);
		}
		this.additionalItemList.add(additionalItem);
	}

	public Etc(Map<String, String> etcMap, User user) {
		this.user = user;
		this.date = LocalDate.parse(etcMap.get("date"), DateTimeFormatter.ISO_DATE);
		this.time = LocalTime.parse(etcMap.get("time"));
		this.memo = etcMap.get("memo");
	}
}
