package controllers;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.varia.NullAppender;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;

import models.Event;
import models.User;

/**
 * Weekly schedule is exported into a PDF File.
 */
public class PDFDocument {

	/**
	 * Creates the PDF document.
	 *
	 * @param user the user for which the weekly calendar is to be created
	 * @param img the image of the chosen weekly calendar stored in a byte array
	 * @param dest the path destination where the pdf is stored	
	 * @param date the date of the week that is to be created
	 * @return the pdf file which consists of the events and an image of the weekly calendar
	 */
	public static File create(User user, byte[] img, String dest, LocalDate date) {
		org.apache.log4j.BasicConfigurator.configure(new NullAppender());

		PdfWriter writer;
		File file;
		try {
			file = new File(dest);
			
			if (!file.exists()) {
				writer = new PdfWriter(dest);
				System.out.println("file was not yet in directory");
			} else {
				int dest_counter = 1;
				dest = dest.substring(0, dest.length() - 4);
				String dest_tmp = "";
				while (file.exists()) {
					dest_tmp = dest + " (" + String.valueOf(dest_counter++) + ")" + ".pdf";
					file = new File(dest_tmp);
				}
				System.out.println("file was already in directory");
				writer = new PdfWriter(dest_tmp);
			}
			PdfDocument pdfDoc = new PdfDocument(writer);
			pdfDoc.addNewPage();
			Document document = new Document(pdfDoc);

			Text title = new Text("Weekly Events for " + user.getUsername());

			addParagraph(document, title, true);

			LocalDate date_mon = date;
			date_mon = date_mon.with(DayOfWeek.MONDAY);
			LocalDate date_sun = date;
			date_sun = date_sun.with(DayOfWeek.SUNDAY);

			Text from_to = new Text(date_mon.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " - "
					+ date_sun.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

			addParagraph(document, from_to, true);
			addBreak(document);

			date = date.with(DayOfWeek.MONDAY);
			for (int i = 1; i <= 7; i++) {
				addDay(document, date, user);
				date = date.plusDays(1);
			}

			ImageData imageData = ImageDataFactory.create(img);
			Image pdfImg = new Image(imageData);
			document.add(pdfImg);

			document.close();
			System.out.println("PDF Created");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		BasicConfigurator.configure();
		return file;
	}

	/**
	 * Adds the days with the according tables to the pdf document.
	 *
	 * @param document the document that is used for the file
	 * @param date the date of the day for which the table is created
	 * @param user the user for which the pdf file is created
	 */
	public static void addDay(Document document, LocalDate date, User user) {
		String weekday = date.getDayOfWeek().toString();
		Text curr_day = new Text(weekday);
		addParagraph(document, curr_day, false);

		float[] pointColumnWidths = { 150F, 150F, 150F, 150F };
		Table table = new Table(pointColumnWidths);
		table.addCell(new Cell().add("Name"));
		table.addCell(new Cell().add("Location"));
		table.addCell(new Cell().add("Time"));
		table.addCell(new Cell().add("Duration"));
		for (Event event : user.getEvents()) {
			if (event.getDate().equals(date)) {
				Cell cell = new Cell().add(event.getName());
				cell.setBackgroundColor(new DeviceGray(0.93f));
				table.addCell(cell);
				cell = new Cell().add(event.getLocation().getName());
				cell.setBackgroundColor(new DeviceGray(0.93f));
				table.addCell(cell);
				cell = new Cell().add(event.getTime().format(DateTimeFormatter.ofPattern("HH:mm")));
				cell.setBackgroundColor(new DeviceGray(0.93f));
				table.addCell(cell);
				cell = new Cell().add(String.valueOf(event.getDurationMinutes()) + " min");
				cell.setBackgroundColor(new DeviceGray(0.93f));
				table.addCell(cell);
			}
		}
		document.add(table);
		addBreak(document);
	}

	/**
	 * Adds a break to the pdf document.
	 *
	 * @param document the document
	 */
	public static void addBreak(Document document) {
		Paragraph break_n = new Paragraph("\n");
		document.add(break_n);
	}

	/**
	 * Gets the font which is used for the paragraphs.
	 *
	 * @return the font
	 */
	public static PdfFont getFont() {
		try {
			PdfFont font;
			font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			return font;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Adds a paragraph to the document.
	 *
	 * @param document the document in which the paragraph is added
	 * @param text the text that is inserted
	 * @param big sets the size of the text. 20f for headers, 15f for regular text
	 */
	public static void addParagraph(Document document, Text text, boolean big) {
		text.setFont(getFont());
		if (big)
			text.setFontSize(20f);
		else
			text.setFontSize(15f);
		Paragraph paragraph = new Paragraph();
		paragraph.add(text);
		document.add(paragraph);
	}

}
