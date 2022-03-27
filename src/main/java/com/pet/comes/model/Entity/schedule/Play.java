package com.pet.comes.model.Entity.schedule;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("PLAY")
@PrimaryKeyJoinColumn(name = "schedule_id")
@NoArgsConstructor
public class Play extends Schedule {

}
