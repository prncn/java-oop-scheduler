package controllers;

import models.Event;
import models.Location;
import models.Priority;
import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Button;
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
      TextField endField, TextField locationField, ArrayList<User> participants, Priority priority, ArrayList<File> attachments) {
    String eventName = titleField.getText();
    LocalDate eventDate = LocalDate.parse(dateField.getText());
    LocalTime eventTime = LocalTime.parse(startField.getText());
    int eventDuration = FormatUtil.parseDuration(startField.getText(), endField.getText());
    String locationName = locationField.getText();
    Location location = new Location(locationName);
    return new Event(eventName, eventDate, eventTime, eventDuration, location, participants, priority, attachments);
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
    Event event = new Event("Test", date, time, duration, new Location("Testtown"), null, Priority.LOW, null);
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

  /** Validate input
   *
   * @param FieldMap Map of textfields
   * @param errorLb Map of error labels
   * @param dpdwn location dropdown button
   * @param selectedPriority selected Priority
   * @return boolean whether input is right or not
   */
  public static boolean validateForm(HashMap<String, TextField> FieldMap, HashMap<String, Label> errorLb, Button dpdwn, Priority selectedPriority) {
    boolean valid = true;
    CompoundBorder border = new CompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
            BorderFactory.createEmptyBorder(4,4,4,4));
    Point contentBox = new Point(40,170);

    TextField titleField = FieldMap.get("titleField");
    TextField dateField = FieldMap.get("dateField");
    TextField startField = FieldMap.get("startField");
    TextField endField = FieldMap.get("endField");
    TextField locationField = FieldMap.get("locationField");
    TextField reminderField = FieldMap.get("reminderField");

    Label errorPriority = errorLb.get("errorPriority");
    Label errorTitle = errorLb.get("errorTitle");
    Label errorDate = errorLb.get("errorDate");
    Label errorStartTime = errorLb.get("errorStart");
    Label errorEndTime = errorLb.get("errorEnd");
    Label errorLocation = errorLb.get("errorLocation");
    Label errorReminder = errorLb.get("errorReminder");
    Label errorMsg = errorLb.get("errorMsg");
    Label WhereLabel = errorLb.get("Where");

    if (selectedPriority == null) {
      errorPriority.setText("(Select Priority)");
      errorPriority.setForeground(Color.RED);
      errorPriority.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorPriority.setText("");
    }


    if (isBlankString(titleField.getText())) {
      titleField.setBorder(border);
      errorTitle.setText("Topic required");
      errorTitle.setForeground(Color.RED);
      errorTitle.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorTitle.setText("");
      titleField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }


    if (isBlankString(dateField.getText())) {
      dateField.setBorder(border);
      errorDate.setText("Select a date");
      errorDate.setForeground(Color.RED);
      errorDate.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorDate.setText("");
      dateField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    if (isBlankString(startField.getText()) || !isValidTime(startField.getText())) {
      startField.setBorder(border);
      errorStartTime.setText("invalid time");
      errorStartTime.setForeground(Color.RED);
      errorStartTime.setBounds(contentBox.x, contentBox.y + 132, startField.getWidth(), startField.getHeight());
      errorStartTime.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorStartTime.setText("");
      startField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    if (isBlankString(endField.getText()) || !isValidTime(endField.getText())) {
      endField.setBorder(border);
      errorEndTime.setText("invalid Time");
      errorEndTime.setForeground(Color.RED);
      errorEndTime.setBounds(contentBox.x + endField.getWidth() + 4, contentBox.y + 132, endField.getWidth(),
              endField.getHeight());
      errorEndTime.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorEndTime.setText("");
      endField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }

    if(isValidTime(startField.getText()) && isValidTime(endField.getText())) {
      if(LocalTime.parse(startField.getText()).isAfter(LocalTime.parse(endField.getText()))) {
        int y = 20;
        startField.setBorder(border);
        endField.setBorder(border);
        errorMsg.setLocation(contentBox.x, contentBox.y + 200);
        errorMsg.setForeground(Color.RED);
        errorMsg.setText("Start Time can't be after End Time");

        WhereLabel.setLocation(contentBox.x, contentBox.y +210 +y);
        errorLocation.setLocation(contentBox.x +50, contentBox.y +210 +y);
        locationField.setLocation(contentBox.x, contentBox.y + 210 + 2*y);
        dpdwn.setLocation(contentBox.x + locationField.getWidth(), contentBox.y + 210 + 2*y);
        valid = false;
      } else {
        startField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        endField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        errorMsg.setText("");

        WhereLabel.setLocation(contentBox.x, contentBox.y +210);
        errorLocation.setLocation(contentBox.x +50, contentBox.y +210);
        locationField.setLocation(contentBox.x, contentBox.y + 210+20 );
        dpdwn.setLocation(contentBox.x + locationField.getWidth(), contentBox.y + 210 +20);
      }
    }

    if (isBlankString(locationField.getText())) {
      locationField.setBorder(border);
      errorLocation.setText("Location required");
      errorLocation.setForeground(Color.RED);
      errorLocation.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorLocation.setText("");
      locationField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
    }


    if (isBlankString(reminderField.getText())) {
      reminderField.setBorder(border);
      errorReminder.setText("Select a reminder");
      errorReminder.setForeground(Color.RED);
      valid = false;
    } else {
      errorReminder.setText("");
      reminderField.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
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

  /**
   * Check if a string is blank or not
   *
   * @param string - the string to be checked
   * @return Boolean whether string is blanked or not
   */
  private static boolean isBlankString(String string) {
    return string == null || string.trim().isEmpty();
  }

}
