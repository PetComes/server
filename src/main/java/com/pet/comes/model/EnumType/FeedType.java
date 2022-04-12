package com.pet.comes.model.EnumType;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public enum FeedType {
	DRY {
		@Override
		public String toString() {
			return "DRY";
		}
	},
	WET {
		@Override
		public String toString() {
			return "WET";
		}
	};

	public static FeedType checkEnumValue(String type) {
		if(type == null) {
			return null;
		}

		List<FeedType> feedTypes = new ArrayList<>();
		feedTypes.add(FeedType.DRY);
		feedTypes.add(FeedType.WET);

		return feedTypes.stream()
			.filter(x -> x.toString().equals(type))
			.findFirst()
			.orElseThrow(
				() -> new NoSuchElementException(
					"FeedType - checkEnumValue (type : " + type + ")" + "type은 DRY, WET 중 하나여야 합니다."));
	}
}
