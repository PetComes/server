package com.pet.comes.model.Entity.schedule;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.pet.comes.model.Entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "etc_item")
public class EtcItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id")
	private Schedule schedule;

	@Column(name="`key`")
	private String key;
	private String value;

	/** 양방향 매핑 */
	public void changeSchedule(Schedule schedule) {
		if(this.schedule != null) {
			this.schedule.getEtcItems().remove(this);
		}
		this.schedule = schedule;
		if (!schedule.getEtcItems().contains(this)) {
			schedule.getEtcItems().add(this);
		}
	}

	/** 생성자 */
	public EtcItem(User user, String key, String value, Schedule schedule) {
		changeSchedule(schedule);
		this.key = key;
		this.value = value;
	}

	/** 수정자 */
	public void modifyValue(String value) {
		this.value = value;
	}
}
