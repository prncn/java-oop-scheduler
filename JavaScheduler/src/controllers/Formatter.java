package controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

public class Formatter {
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
	  
}
