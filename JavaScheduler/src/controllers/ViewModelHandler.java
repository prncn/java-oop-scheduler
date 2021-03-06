package controllers;

import models.Event;
import models.*;
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
import java.util.Locale;
import java.util.Random;

/**
 * The view model handler contains methods that serve as interaction between model
 * and view. For instance, it might bring higher level logic out of the view
 * to then, turn into model entities.
 */
public class ViewModelHandler {

  /**
   * Consume input form data from Create view and generate a model Event.
   * This method is used at the end of the schedule event form.
   *
   * @param FieldMap    -  Map of text fields which is passed
   * @param participants - list of participants
   * @param reminder    - selected reminder
   * @param priority    - selected priority
   * @param attachments - list of attachments
   * @param descField   - description field
   * @return Event object from given data
   */
  public static Event consumeEventForm( HashMap<String, TextField> FieldMap, ArrayList<User> participants, Reminder reminder, Priority priority,
      ArrayList<File> attachments, JTextArea descField) {

    TextField titleField = FieldMap.get("titleField");
    TextField dateField = FieldMap.get("dateField");
    TextField startField = FieldMap.get("startField");
    TextField endField = FieldMap.get("endField");
    TextField locationField = FieldMap.get("locationField");

    String eventName = titleField.getText();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[MMMM dd, yyyy]" + "[yyyy-MM-dd]", Locale.US);
    LocalDate eventDate = LocalDate.parse(dateField.getText(), formatter);
    LocalTime eventTime = LocalTime.parse(startField.getText());
    int eventDuration = FormatUtil.parseDuration(startField.getText(), endField.getText());
    String locationName = locationField.getText();
    Location location = new Location(locationName);
    String description = descField.getText();


    return new Event(eventName, eventDate, eventTime, eventDuration, location, participants, reminder, priority,
        attachments, description);
  }

  /**
   * Update and redraw calendar layout on events. Method gets called on event
   * change.
   * @param frame - frame which is used to place calendar on.
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
   * Update profile avatar icon on sidebar incase of
   * changes in other views.
   * @param user - Currently logged in user
   */
  public static void updateProfileIcon(User user) {
    HomeUI.sidebarAvatar.fillIcon(FormatUtil.resizeImageIcon(user.getAvatar(), 0.7f));
  }

  /**
   * Create dummy event for testing. Date and time are randomised.
   * 
   * @return Test event object
   */
  public static Event createTestEvent() {
    Random rand = new Random();
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-M-d");
    DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("H:mm");
    LocalDate date = LocalDate.parse("2021-01-" + String.valueOf(rand.nextInt(31) + 1), dateFormat);
    LocalTime time = LocalTime.parse(String.valueOf(rand.nextInt(17 - 9) + 9) + ":00", timeFormat);
    int duration = rand.nextInt(190 - 30) + 30;
    Event event = new Event("Test", date, time, duration, new Location("Testtown"), null, Reminder.NONE, Priority.LOW,
        null, "");
    event.setParticipants(new ArrayList<User>());
    return event;
  }

  /**
   * User search field of view to search users from database
   * 
   * @param searchField     - TextField for query input, takes in usernames
   * @param panel           - Panel to be placed on
   * @param userQueryResult - Label giving error or succes messages on query
   *                        result
   * @return - User object, null if not found
   */
  public static User searchUser(TextField searchField, Panel panel, Label userQueryResult) {
    if (searchField.getText().isEmpty()) {
      return null;
    }
    User user = DatabaseAPI.getUser(searchField.getText());
    if (userQueryResult == null) {
      return user;
    }
    userQueryResult.setLocation(searchField.getX() + searchField.getWidth() - userQueryResult.getWidth() + 40,
        searchField.getY() + 40);
    userQueryResult.setHorizontalAlignment(SwingConstants.RIGHT);
    userQueryResult.setUnset(true);
    userQueryResult.setText("");
    if (user != null) {
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
   * Validate input of form. Display UI errors on the form
   * incase of missing or invalid inputs.
   *
   * @param FieldMap         Map of textfields
   * @param selectedPriority selected Priority
   * @return boolean whether input is right or not
   */
  public static boolean validateForm(HashMap<String, TextField> FieldMap, Priority selectedPriority) {
    boolean valid = true;
    CompoundBorder border = new CompoundBorder(BorderFactory.createLineBorder(Color.RED, 1),
        BorderFactory.createEmptyBorder(9, 9, 9, 9));

    TextField titleField = FieldMap.get("titleField");
    TextField dateField = FieldMap.get("dateField");
    TextField startField = FieldMap.get("startField");
    TextField endField = FieldMap.get("endField");
    TextField priorityField = FieldMap.get("priorityField");
    TextField locationField = FieldMap.get("locationField");
    TextField reminderField = FieldMap.get("reminderField");

    TextField[] fields = { titleField, dateField, startField, endField, locationField, reminderField };

    for (TextField field : fields) {
      if (FormatUtil.isBlankString(field.getText())) {
        field.setBorder(border);
        field.getErrorLabel().setText("*Select " + field.getErrorLabel().getName());
        valid = false;
      } else {
        field.getErrorLabel().setText("");
        field.setDefaultStyle();
      }
    }

    if(selectedPriority == null) {
      priorityField.getErrorLabel().setText("*Select " + priorityField.getErrorLabel().getName());
      valid = false;
    } else {
      priorityField.getErrorLabel().setText("");
      priorityField.setDefaultStyle();
    }

    if (isValidTime(startField.getText()) && isValidTime(endField.getText())) {
      if (LocalTime.parse(startField.getText()).isAfter(LocalTime.parse(endField.getText()))) {
        startField.setBorder(border);
        endField.setBorder(border);
        endField.getErrorLabel().setText("*Start time can't be after end time");
        valid = false;
      } else {
        startField.setDefaultStyle();
        endField.setDefaultStyle();
        endField.getErrorLabel().setText("");
      }
    } else if (!isValidTime(startField.getText()) | !isValidTime(endField.getText())) {
        if(!isValidTime(startField.getText())) {
            startField.setBorder(border);
            startField.getErrorLabel().setText("*Select " + startField.getErrorLabel().getName());
            valid = false;
          }
        if(!isValidTime(endField.getText())) {
            endField.setBorder(border);
            endField.getErrorLabel().setText("*Select " + endField.getErrorLabel().getName());
            valid = false;
          }
        } else {
          startField.setDefaultStyle();
          startField.getErrorLabel().setText("");
          endField.setDefaultStyle();
          endField.getErrorLabel().setText("");
        }

    if (!isValidDate(dateField.getText())) {
      dateField.setBorder(border);
      dateField.getErrorLabel().setText("*Select " + dateField.getErrorLabel().getName());
      valid = false;
    } else {
      dateField.setDefaultStyle();
      dateField.getErrorLabel().setText("");
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
   * Validate if given string is of pattern "YYYY-MM-DD"
   * @param date - the string to be checked
   * @return Boolean whether form is valid or not
   */
  private static boolean isValidDate(String date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("[MMMM dd, yyyy]" + "[yyyy-MM-dd]", Locale.US);
    try {
      LocalDate.parse(date, formatter);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }
}
