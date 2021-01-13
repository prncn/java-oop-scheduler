package controllers;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import models.Event;
import models.Location;
import models.Priority;
import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;
import views.panels.Dashboard;
import views.panels.ProfilePanel;

public class ViewModelHandler {

  /**
   * Consume input form data from Create view and generate a model Event
   * 
   * @param titleField    - Field for title
   * @param dateField     - Field for date
   * @param durationField - Field for duration
   * @param locationField - Field for location
   * @return Event object from given data
   */
  public static Event consumeEventForm(TextField titleField, TextField dateField, TextField startField,
      TextField endField, TextField locationField, ArrayList<User> participants, Priority priority, TextField attachField) {
    String eventName = titleField.getText();
    LocalDate eventDate = LocalDate.parse(dateField.getText());
    LocalTime eventTime = LocalTime.parse(startField.getText());
    int eventDuration = FormatUtil.parseDuration(startField.getText(), endField.getText());
    String locationName = locationField.getText();
    Location location = new Location(locationName);
    File attachment = new File(attachField.getText());
    return new Event(eventName, eventDate, eventTime, eventDuration, location, participants, priority, attachment);
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
    User user = DataBaseAPI.getUser(searchField.getText());
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

}
