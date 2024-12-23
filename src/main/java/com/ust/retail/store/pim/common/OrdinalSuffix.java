package com.ust.retail.store.pim.common;

import org.springframework.stereotype.Component;

@Component
public class OrdinalSuffix {

	public static String getOrdinalSuffixForDate(String day) {
		int date = Integer.parseInt(day);
		if (date < 1 || date > 31) {
			throw new IllegalArgumentException("Date must be between 1 and 31");
		}
		
		int lastDigit = date % 10;
		int lastTwoDigits = date % 100;
		
		if (lastTwoDigits >= 11 && lastTwoDigits <= 13) {
			return date + "th";
		}
		
		switch (lastDigit) {
		case 1:
			return date + "st";
		case 2:
			return date + "nd";
		case 3:
			return date + "rd";
		default:
			return date + "th";
		}
	}
}
