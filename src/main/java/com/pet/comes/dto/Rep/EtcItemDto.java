package com.pet.comes.dto.Rep;

import com.pet.comes.model.Entity.schedule.EtcItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EtcItemDto {
	private Long id;
	private String key;
	private String value;

	public static EtcItemDto convertToEtcItemDto(EtcItem etcItem) {
		return EtcItemDto.builder()
			.id(etcItem.getId())
			.key(etcItem.getKey())
			.value(etcItem.getValue())
			.build();
	}
}
