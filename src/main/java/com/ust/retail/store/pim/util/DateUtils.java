package com.ust.retail.store.pim.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Optional;

public class DateUtils {
	
	private DateUtils() {
	}

	public static String dateCurrentyyyyMMddHHmmssSS() {
		DateFormat hourdateFormat = new SimpleDateFormat("yyyyMMddHHmmssSS");
		return hourdateFormat.format(new Date());
	}

	public static String dateFormatddMMyyyy(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(date);
	}

	public static String dateAmericanFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static Date dateFormatForDatabase(String stringDate) {
		Date result = null;
		try {
			if (stringDate != null)
				result = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String dateFormatEEEMMMMyyyy(Date date) {
		SimpleDateFormat formatter = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy 'a' HH:mm",
				new Locale("es", "MX"));
		return formatter.format(date);
	}

	public static Date parseStringToDate(String stringDate) {
		Date result = null;
		try {
			if (stringDate != null)
				result = new SimpleDateFormat("dd/MM/yyyy").parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date parseToDateTime(String stringDate) {
		Date result = null;
		try {
			if (stringDate != null && !stringDate.equalsIgnoreCase("null") && stringDate.length() == 16)
				result = new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(stringDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Date atStartOfDay(Date date) {
		return atStartOfDay(date, ZoneId.systemDefault());
	}

	public static Date atStartOfDay(Date date, ZoneId targetZone) {
		return Optional.ofNullable(date)
				.map(d -> getDateAtStartOfDayAtZone(d, targetZone))
				.orElse(null);
	}

	public static Date atEndOfDay(Date date) {
		return atEndOfDay(date, ZoneId.systemDefault());
	}

	public static Date atEndOfDay(Date date, ZoneId targetZone) {
		return Optional.ofNullable(date)
				.map(d -> getDateAtEndOfDayAtZone(d, targetZone))
				.orElse(null);
	}

	private static Date getDateAtStartOfDayAtZone(Date d, ZoneId targetZone) {
		return new Date(LocalDate.ofInstant(d.toInstant(), targetZone)
				.atStartOfDay(targetZone)
				.toInstant()
				.toEpochMilli());
	}

	private static Date getDateAtEndOfDayAtZone(Date d, ZoneId targetZone) {
		return new Date(LocalDate.ofInstant(d.toInstant(), targetZone)
				.atTime(LocalTime.MAX)
				.atZone(targetZone)
				.toInstant()
				.toEpochMilli());
	}

	public static Integer daysBetween(Date date1, Date date2) {
		return Math.toIntExact(Math.abs(ChronoUnit.DAYS.between(date1.toInstant(), date2.toInstant())));
	}

	public static String getAmPmFormattedTime(Integer time) {
		int hour = time / 100;
		int minute = time % 100;
		if (hour > 23 || hour < 0) {
			throw new IllegalArgumentException(String.format("Invalid hour: %d", hour));
		}
		if (minute > 59) {
			throw new IllegalArgumentException(String.format("Invalid minute: %d", minute));
		}
		return LocalTime.of(hour, minute).format(DateTimeFormatter.ofPattern("hh:mm a", Locale.US));
	}

	public static String getDayName(int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK, day);

		return new SimpleDateFormat("EEEE", Locale.US).format(calendar.getTime());
	}

	public static Integer getCurrentTimeAsInt() {
		Calendar calendar = Calendar.getInstance();
		
//		System.out.println("Todays Date getCurrentTimeAsInt: {0}" + calendar);

		calendar.add(Calendar.HOUR, -2);
//		System.out.println("Todays Date getCurrentTimeAsInt: {-2}" + calendar);

		return (calendar.get(Calendar.HOUR_OF_DAY) * 100) + calendar.get(Calendar.MINUTE);
	}
}
