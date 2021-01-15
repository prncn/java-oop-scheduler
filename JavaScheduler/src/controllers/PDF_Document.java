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

public class PDF_Document {
	public PDF_Document() {}
	public static void create(User user, byte[] img, String dest){
		org.apache.log4j.BasicConfigurator.configure(new NullAppender());
		
		
		PdfWriter writer;
		try {
			File file = new File(dest);
			
			if (!file.exists()) {
				   writer = new PdfWriter(dest); //file is directory
				   System.out.println("file was not yet in directory");
				}
			else {
				int dest_counter = 1;
				while(file.exists()) {
					dest = dest + String.valueOf(dest_counter++) + ".pdf";
					file = new File(dest);
				}
				System.out.println("file was already in directory");
				writer = new PdfWriter(dest);
			}
			
			PdfDocument pdfDoc = new PdfDocument(writer);
			pdfDoc.addNewPage();
			Document document = new Document(pdfDoc);
			
			Text title = new Text("Weekly Events for " + user.getUsername());
			
			addParagraph(document, title, true);
			
			LocalDate date_mon = LocalDate.now();
			date_mon = date_mon.with(DayOfWeek.MONDAY);
			LocalDate date_sun = LocalDate.now();
			date_sun = date_sun.with(DayOfWeek.SUNDAY);
			
			Text from_to = new Text(date_mon.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +" - " + date_sun.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
			
			addParagraph(document, from_to, true);
			
			addBreak(document);
			
			for(int i = 1;i<=7;i++) {
				addDay(document, i, user);
			}
			
			ImageData imageData = ImageDataFactory.create(img);
			Image pdfImg = new Image(imageData);
			document.add(pdfImg);

			document.close();
			
			System.out.println("PDF Created");
			
			
;		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
}
		
		BasicConfigurator.configure();
	}
	public static void addDay(Document document, int day, User user) {
		String weekday = "";
		LocalDate date = LocalDate.now();
		//date = date.with(DayOfWeek.MONDAY);
		switch(day) {
		case 1: date = date.with(DayOfWeek.MONDAY);weekday = "Monday";break;
		case 2: date = date.with(DayOfWeek.TUESDAY);weekday = "Tueday";break;
		case 3: date = date.with(DayOfWeek.WEDNESDAY);weekday = "Wednesday";break;
		case 4: date = date.with(DayOfWeek.THURSDAY);weekday = "Thursday";break;
		case 5: date = date.with(DayOfWeek.FRIDAY);weekday = "Friday";break;
		case 6: date = date.with(DayOfWeek.SATURDAY);weekday = "Saturday";break;
		case 7: date = date.with(DayOfWeek.SUNDAY);weekday = "Sunday";break;
		default : System.out.println("Error in WeekDate/ PDF-Creation");break;
		}
		Text curr_day = new Text(weekday);
		
		addParagraph(document, curr_day, false);
		
		float [] pointColumnWidths = {150F, 150F,150F,150F};
		Table table = new Table(pointColumnWidths);
		table.addCell(new Cell().add("Name"));
		table.addCell(new Cell().add("Date"));
		table.addCell(new Cell().add("Time"));
		table.addCell(new Cell().add("Duration"));
		for(Event event : user.getAcceptedEvents()) {
			if(event.getDate().equals(date)) {
				Cell cell = new Cell().add(event.getName());
				cell.setBackgroundColor(new DeviceGray(0.93f));
				table.addCell(cell);
				cell = new Cell().add(event.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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
	public static void addBreak(Document document) {
		Paragraph break_n = new Paragraph("\n");
		
		document.add(break_n);
	}
	public static PdfFont getFont() {
		try {
			PdfFont font;
			font = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
			return font;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		return null;
	}
	
	public static void addParagraph(Document document, Text text, boolean big) {

		text.setFont(getFont());
		if(big)text.setFontSize(20f);
		else text.setFontSize(15f);
		Paragraph paragraph = new Paragraph();
		paragraph.add(text);
		document.add(paragraph);
	}
	

}
