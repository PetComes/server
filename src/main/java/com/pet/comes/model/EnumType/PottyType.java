package com.pet.comes.model.EnumType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public enum PottyType {
	URINE {
		@Override
		public String toString() {
			return "URINE";
		}

	},
	FECES {
		@Override
		public String toString() {
			return "FECES";
		}
	};

	public static PottyType getValidatedEnumValue(String kind) {
		if(kind == null) {
			return null;
		}

		List<PottyType> pottyTypes = new ArrayList<>();
		pottyTypes.add(PottyType.URINE);
		pottyTypes.add(PottyType.FECES);

		return pottyTypes.stream()
			.filter(x -> x.toString().equals(kind))
			.findFirst()
			.orElseThrow(
				() -> new NoSuchElementException(
					"PottyType - checkEnumValue (kind : " + kind + ")" + "kind는 URINE, FECES 중 하나여야 합니다."));
	}
}
