package com.pet.comes.model.Entity.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "additional_item")
public class AdditionalItem {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "etc_id")
	private Etc etc;

	private String key; // 추가되는 항목 VARCHAR(255)

	@Column(columnDefinition = "TEXT")
	private String value; // 항목의 값
}