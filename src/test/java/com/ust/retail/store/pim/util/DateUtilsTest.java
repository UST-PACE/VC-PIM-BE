package com.ust.retail.store.pim.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;

class DateUtilsTest {

	@Test
	void dateCurrentyyyyMMddHHmmssSSReturnsPartialExpected() {
		String partialResult = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

		String result = DateUtils.dateCurrentyyyyMMddHHmmssSS();

		assertThat(result, startsWith(partialResult));
	}

	@Test
	void dateFormatddMMyyyyReturnsExpected() {
		Date date = new Date();
		String expected = new SimpleDateFormat("dd/MM/yyyy").format(date);

		String result = DateUtils.dateFormatddMMyyyy(date);

		assertThat(result, is(expected));
	}

	@Test
	void dateAmericanFormatReturnsExpected() {
		Date date = new Date();
		String expected = new SimpleDateFormat("yyyy-MM-dd").format(date);

		String result = DateUtils.dateAmericanFormat(date);

		assertThat(result, is(expected));
	}

	@Test
	void dateFormatForDatabaseReturnsNullWhenStringDateIsNull() {
		Date result = DateUtils.dateFormatForDatabase(null);

		assertThat(result, is(nullValue()));
	}

	@Test
	void dateFormatForDatabaseReturnsNullWhenStringDateIsInvalid() {
		Date result = DateUtils.dateFormatForDatabase("INVALID DATE");

		assertThat(result, is(nullValue()));
	}

	@Test
	void dateFormatForDatabaseReturnsExpectedWhenStringDateIsValid() {
		Date dbDate = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String stringDate = dateFormatter.format(dbDate);

		Date result = DateUtils.dateFormatForDatabase(stringDate);

		assertThat(dateFormatter.format(result), is(stringDate));
	}

	@Test
	void dateFormatEEEMMMMyyyyReturnsExpected() {
		Date date = new Date();
		String expected = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy 'a' HH:mm",
				new Locale("es", "MX")).format(date);

		String result = DateUtils.dateFormatEEEMMMMyyyy(date);

		assertThat(result, is(expected));
	}

	@Test
	void parseStringToDateReturnsNullWhenStringDateIsNull() {
		Date result = DateUtils.parseStringToDate(null);

		assertThat(result, is(nullValue()));
	}

	@Test
	void parseStringToDateReturnsNullWhenStringDateIsInValid() {
		String stringDate = "INVALID DATE";

		Date result = DateUtils.parseStringToDate(stringDate);

		assertThat(result, is(nullValue()));
	}

	@Test
	void parseStringToDateReturnsExpectedWhenStringDateIsValid() {
		Date dbDate = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		String stringDate = dateFormatter.format(dbDate);

		Date result = DateUtils.parseStringToDate(stringDate);

		assertThat(dateFormatter.format(result), is(stringDate));
	}

	@Test
	void parseToDateTimeReturnsNullWhenStringDateIsInvalid() {
		Arrays.stream(new String[]{null, "null", "Non 16 length string", "16 length string"})
				.forEach(this::invokeParseToDateTimeAndAssertNull);
	}

	private void invokeParseToDateTimeAndAssertNull(String invalidString) {
		Date result = DateUtils.parseToDateTime(invalidString);

		assertThat(result, is(nullValue()));
	}

	@Test
	void parseToDateTimeReturnsExpectedWhenStringDateIsValid() {
		Date dbDate = new Date();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		String stringDate = dateFormatter.format(dbDate);

		Date result = DateUtils.parseToDateTime(stringDate);

		assertThat(dateFormatter.format(result), is(stringDate));
	}

	@Test
	void atStartOfDayReturnsExpectedWhenSimpleDate() throws Exception {
		SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date result = DateUtils.atStartOfDay(inputSdf.parse("2021-10-03"));

		assertThat(outputSdf.format(result), is("2021-10-03 00:00:00"));
	}

	@Test
	void atStartOfDayReturnsExpectedWhenFullDate() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date result = DateUtils.atStartOfDay(sdf.parse("2021-10-03 20:25:56"));

		assertThat(sdf.format(result), is("2021-10-03 00:00:00"));
	}

	@Test
	void atStartOfDayReturnsExpectedWhenNullDate() {
		Date result = DateUtils.atStartOfDay(null);

		assertThat(result, is(nullValue()));
	}

	@Test
	void atEndOfDayReturnsExpectedWhenSimpleDate() throws Exception {
		SimpleDateFormat inputSdf = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat outputSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date result = DateUtils.atEndOfDay(inputSdf.parse("2021-10-03"));

		assertThat(outputSdf.format(result), is("2021-10-03 23:59:59"));
	}

	@Test
	void atEndOfDayReturnsExpectedWhenFullDate() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Date result = DateUtils.atEndOfDay(sdf.parse("2021-10-03 20:25:56"));

		assertThat(sdf.format(result), is("2021-10-03 23:59:59"));
	}

	@Test
	void atEndOfDayReturnsExpectedWhenNullDate() {
		Date result = DateUtils.atEndOfDay(null);

		assertThat(result, is(nullValue()));
	}
}
