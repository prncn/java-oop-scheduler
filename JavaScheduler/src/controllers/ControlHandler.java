package controllers;

import java.time.LocalDate;

import models.Event;
import models.Location;
import views.components.TextField;

public class ControlHandler {

  /**
   * Consume input form data from Create view and generate a model Event
   * 
   * @param titleField    - Field for title
   * @param dateField     - Field for date
   * @param durationField - Field for duration
   * @param locationField - Field for location
   * @return Event object from given data
   */
  public static Event consumeEventForm(TextField titleField, TextField dateField, TextField durationField,
      TextField locationField) {
    String eventName = titleField.getText();
    LocalDate eventDate = LocalDate.parse(dateField.getText());
    int eventDuration = Integer.parseInt(durationField.getText());
    String locationName = locationField.getText();
    Location location = new Location(locationName);
    return new Event(eventName, eventDate, eventDuration, location);
  }

}
