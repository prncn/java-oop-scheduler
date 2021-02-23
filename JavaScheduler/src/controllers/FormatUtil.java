package controllers;

import models.Event;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;

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
	 * @param startStr - Beginning time stamp
	 * @param endStr   - Ending time stamp
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
	 * Check if a string is blank or not
	 *
	 * @param string - the string to be checked
	 * @return Boolean whether string is blanked or not
	 */
	public static boolean isBlankString(String string) {
		return string == null || string.trim().isEmpty();
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
	 * Parse LocalDate to readable string Format. For instance,
	 * <code>2021-03-05</code> gets parsed to <code>March 5</code>.
	 * 
	 * @param date - LocalDate object
	 * @return String of date
	 */
	public static String readableDate(LocalDate date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, YYYY", Locale.US);
		return date.format(formatter);
	}

	/**
	 * Get ending time from a giving starting time and duration.
	 * 
	 * @param startTime    - Starting LocalTime of event
	 * @param durationMins - Duration in minutes
	 * @return LocalTime of ending time
	 */
	public static LocalTime getEndTime(Event event) {
		return event.getTime().plusMinutes((long) event.getDurationMinutes());
	}

	/**
	 * Resize an image to a specified factor and retain its resolution
	 * 
	 * @param img        - Source ImageIcon to be resized
	 * @param proportion - Resize factor to resize image
	 * @return Copy of img in resized dimensions
	 */
	public static ImageIcon resizeImageIcon(ImageIcon img, float proportion) {
		int width = img.getIconWidth();
		int height = img.getIconHeight();

		width = Math.round(width * proportion);
		height = Math.round(height * proportion);

		Image image = img.getImage();
		Image newimg = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		return new ImageIcon(newimg);
	}

	/**
	 * Convert Image object to ImageIcon object cropped to circular shape and
	 * 128x128 dimensions for profile icon use from any user image. Used to fetch
	 * from database and bring to view.
	 * 
	 * @param img - Image to be converted
	 * @return ImageIcon object with proper attributes
	 */
	public static ImageIcon byteToIcon(byte[] bytes) {
		InputStream is = new ByteArrayInputStream(bytes);
		BufferedImage original = null;
		try {
			original = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		BufferedImage resized = new BufferedImage(128, 128, BufferedImage.TYPE_INT_ARGB);
		if (original.getWidth() > original.getHeight()) {
			original = original.getSubimage(original.getWidth() / 2 - original.getHeight() / 2, 0, original.getHeight(),
					original.getHeight());
		} else {
			original = original.getSubimage(0, original.getHeight() / 2 - original.getWidth() / 2, original.getWidth(),
					original.getWidth());
		}
		Graphics2D g = resized.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.fillOval(1, 1, 126, 126);
		g.setComposite(AlphaComposite.SrcIn);
		g.drawImage(original, 0, 0, 128, 128, null);
		g.dispose();

		return new ImageIcon(resized);
	}

	/**
	 * Convert File object to array of bytes. This method is used to select an image
	 * from files and import it into the UI view.
	 * 
	 * @param img - File object of selected image
	 * @return Byte array containing image stream data
	 */
	public static byte[] fileToBytes(File img) {
		try {
			BufferedImage image = ImageIO.read(img);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", baos);
			byte[] buffByte = baos.toByteArray();
			return buffByte;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Convert ImageIcon image to array of bytes. This method is used to prepare
	 * storing icons to the database.
	 * 
	 * @param icon - ImageIcon to be converted
	 * @return Byte array containing image stream data
	 */
	public static byte[] iconToBytes(ImageIcon icon) {
		BufferedImage bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = bi.createGraphics();
		icon.paintIcon(null, g, 0, 0);
		g.dispose();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			ImageIO.write(bi, "jpg", baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	/**
	 * Get file extension name
	 * @param filename - File to get extension from
	 * @return String of file extension
	 */
	public static String getFileExtension(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

}
