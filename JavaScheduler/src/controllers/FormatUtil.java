package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

import java.awt.Color;

public class FormatUtil {
	/**
	 * Parse any date to LocalDate object
	 * 
	 * @param year  - Integer of year
	 * @param month - Integer of month
	 * @param day   - Integer of day
	 * @return - Parsed LocalDate
	 */
	public static LocalDate parseDate(int year, int month, int day) {
		String formattedDate = "";
		try {
			String parseDate = year + "-" + month + "-" + day;
			SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
			Date date = parser.parse(parseDate);
			formattedDate = parser.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return LocalDate.parse(formattedDate);
	}

	/**
	 * Calculate the difference between to time stamps
	 * 
	 * @param startTime - Beginning time stamp
	 * @param endTime   - Ending time stamp
	 * @return Duration from start until end time
	 */
	public static int parseDuration(String startStr, String endStr) {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("H:mm");
		LocalTime startTime = LocalTime.parse(startStr, format);
		LocalTime endTime = LocalTime.parse(endStr, format);
		return (int) startTime.until(endTime, ChronoUnit.MINUTES);
	}

	/**
	 * Correctly format ordinal suffix of a positive number (2 -> 2nd, 43 -> 43rd)
	 * 
	 * @param num - Original number
	 * @return String formatted number
	 */
	public static String formatOrdinal(int num) {
		if (num == 11 || num == 12 || num == 13)
			return num + "th";
		switch (num % 10) {
			case 1:
				return num + "st";
			case 2:
				return num + "nd";
			case 3:
				return num + "rd";
			default:
				return num + "th";
		}
	}

	/**
	 * Function to make Strings capitalized
	 * 
	 * @param input - input String that gets capitalized
	 */
	public static String capitalize(String input) {
		String output = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
		return output;
	}

	/**
	 * Change alpha value (transparency) of given color
	 * 
	 * @param color - Original color
	 * @param alpha - New alpha value
	 * @return Color object copy with set alpha value
	 */
	public static Color colorLowerAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	/**
	 * Parse LocalDate to readable string Format. 
	 * For instance, <code>2021-03-01</code> gets parsed to <code>March 1st</code>.
	 * @param date - LocalDate object
	 * @return String of date
	 */
	public static String readableDate(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM");
		return date.format(formatter);
	}

}
