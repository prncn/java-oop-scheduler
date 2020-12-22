package views;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import views.components.*;

public class createMeetingPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public createMeetingPanel(JFrame frame) {
    super(frame);

    Label screenTitle = new Label(40, 40, "Schedule a Meeting");
    Button confirmBtn = new Button(40, 550, "Confirm", MasterUI.getColor("accentCol"));
    confirmBtn.setTab();
    confirmBtn.setHorizontalAlignment(SwingConstants.CENTER);

    Label eventNameLbl = new Label(40, 100, "Name of Event");
    Label eventDateLbl = new Label(40, 170, "Date of Event");
    Label eventDurationLbl = new Label(40, 240, "Duration of Event");
    Label eventLocationLbl = new Label(40, 310, "Location of Event");

    TextField eventName = new TextField(40, 120);
    TextField eventDate = new TextField(40, 190);
    TextField eventDuration = new TextField(40, 260);
    TextField eventLocation = new TextField(40, 330);

    this.add(screenTitle);
    this.add(eventNameLbl);
    this.add(eventDurationLbl);
    this.add(eventDateLbl);
    this.add(eventLocationLbl);
    this.add(eventName);
    this.add(eventDate);
    this.add(eventDuration);
    this.add(eventLocation);
    this.add(eventName);
    this.add(confirmBtn);

    ((MasterUI) frame).setComponentStyles(this, "light");
    screenTitle.setHeading();
  }
}
