package com.ust.retail.store.pim.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.text.WordUtils;

import pl.allegro.finance.tradukisto.MoneyConverters;

public class ConvertDollarToWords {
	private ConvertDollarToWords() {
	}

	public static String covertToLetters(Double value) {
		
		String valueTwoDecimals =  new DecimalFormat("#.##").format(value);
		
		MoneyConverters converter = MoneyConverters.ENGLISH_BANKING_MONEY_VALUE;
		String valueStr = converter.asWords(new BigDecimal(valueTwoDecimals))
				.replace("£", "USD Dollars");
		
		return WordUtils.capitalize(valueStr, ' ', '-');
	}
}
