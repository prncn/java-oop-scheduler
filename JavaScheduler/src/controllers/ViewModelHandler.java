package controllers;

import models.Event;
import models.Location;
import models.Priority;
import models.Reminder;
import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;
import views.panels.Dashboard;
import views.panels.ProfilePanel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;

import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ViewModelHandler {

  /**
   * Consume input form data from Create view and generate a model Event
   * 
   * @param titleField    - Field for title
   * @param dateField     - Field for date
   * @param startField - Field for Start Time
   * @param endField - Field for End Time
   * @param locationField - Field for location
   * @return Event object from given data
   */
  public static Event consumeEventForm(TextField titleField, TextField dateField, TextField startField,
      TextField endField, TextField locationField, ArrayList<User> participants, Reminder reminder, Priority priority, ArrayList<File> attachments) {
    String eventName = titleField.getText();
    LocalDate eventDate = LocalDate.parse(dateField.getText());
    LocalTime eventTime = LocalTime.parse(startField.getText());
    int eventDuration = FormatUtil.parseDuration(startField.getText(), endField.getText());
    String locationName = locationField.getText();
    Location location = new Location(locationName);
    return new Event(eventName, eventDate, eventTime, eventDuration, location, participants, reminder, priority, attachments);
  }

  /**
   * Update and redraw calendar layout on events. Method gets called on event
   * change.
   */
  public static void updateCalendar(JFrame frame) {
    HomeUI.calendarPanel.changeDateFromTextField(frame);
    HomeUI.calendarPanel.repaint();
  }

  /**
   * Update and redraw event data on dashboard. Method gets called on event
   * change.
   * 
   * @param user - Currently logged in user
   */
  public static void updateDashboard(User user) {
    Dashboard.drawEventData(user);
    ProfilePanel.updateProfileStats(user);
  }

  /**
   * Create dummy event for testing. Date and time are randomised.
   * @return Test event object
   */
  public static Event createTestEvent() {
    Random rand = new Random();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-M-d");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
    LocalDate date = LocalDate.parse("2021-01-" + String.valueOf(rand.nextInt(31) + 1), dateFormat);
    LocalTime time = LocalTime.parse(String.valueOf(rand.nextInt(17-9) + 9) + ":00", timeFormat);
    int duration = rand.nextInt(190 - 30) + 30;
    System.out.println(duration);
    Event event = new Event("Test", date, time, duration, new Location("Testtown"), null, Reminder.NONE, Priority.LOW, null);
    return event;
  }

  /**
   * User search field of view to search users from database
   * @param searchField - TextField for query input, takes in usernames
   * @param panel - Panel to be placed on
   * @param userQueryResult - Label giving error or succes messages on query result
   * @return - User object, null if not found
   */
  public static User searchUser(TextField searchField, Panel panel, Label userQueryResult) {
    if(searchField.getText().isEmpty()){
      return null;
    }
    User user = DatabaseAPI.getUser(searchField.getText());
    userQueryResult.setPosition(searchField.getX(), searchField.getY() + 60);
    userQueryResult.setText("");
    if(user != null){
      userQueryResult.setForeground(MasterUI.secondaryCol);
      userQueryResult.setText("User found");
      userQueryResult.setForeground(MasterUI.secondaryCol);
      searchField.setText("");
      panel.repaint();
    } else {
      userQueryResult.setForeground(MasterUI.primaryColAlt);
      userQueryResult.setText("User not found");
    }
    panel.add(userQueryResult);
    return user;
  }

  /** 
   * Validate input
   *
   * @param FieldMap Map of textfields
   * @param selectedPriority selected Priority
   * @return boolean whether input is right or not
   */
  public static boolean validateForm(HashMap<String, TextField> FieldMap, Priority selectedPriority) {
    boolean valid = true;
    CompoundBorder border = new CompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
            BorderFactory.createEmptyBorder(4,4,4,4));

    TextField titleField = FieldMap.get("titleField");
    TextField dateField = FieldMap.get("dateField");
    TextField startField = FieldMap.get("startField");
    TextField endField = FieldMap.get("endField");
    TextField priorityField = FieldMap.get("priorityField");
    TextField locationField = FieldMap.get("locationField");
    TextField reminderField = FieldMap.get("reminderField");

    TextField[] fields = { titleField, dateField, startField, endField, priorityField, locationField, reminderField };
    for (TextField field : fields) {
      if (FormatUtil.isBlankString(field.getText())) {
        field.setBorder(border);
        field.getErrorLabel().setText("*Select " + field.getErrorLabel().getName());
        valid = false;
      } else {
        field.getErrorLabel().setText("");
        field.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
      }
    }

    if(isValidTime(startField.getText()) && isValidTime(endField.getText())) {
      if(LocalTime.parse(startField.getText()).isAfter(LocalTime.parse(endField.getText()))) {
        startField.setBorder(border);
        endField.setBorder(border);
        endField.getErrorLabel().setLocation(endField.getX() - 100, endField.getY() + endField.getHeight());
        endField.getErrorLabel().setText("*Start time can't be after end time");

        valid = false;
      } else {
        startField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        endField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        endField.getErrorLabel().setText("");
      }
    }

    return valid;
  }

  /**
   * Validate if given string is of pattern "H:mm"
   *
   * @param time - the string to be checked
   * @return Boolean whether form is valid or not
   */
  private static boolean isValidTime(String time) {
    try {
      LocalTime.parse(time);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }

}
