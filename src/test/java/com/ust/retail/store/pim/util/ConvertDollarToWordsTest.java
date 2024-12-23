package com.ust.retail.store.pim.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ConvertDollarToWordsTest {
	@Test
	void covertToLettersReturnsExpected() {
		assertThat(
				ConvertDollarToWords.covertToLetters(1234.56),
				is("One Thousand Two Hundred Thirty-Four USD Dollars 56/100"));
	}
}
