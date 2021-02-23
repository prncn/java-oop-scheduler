package views.panels;

import controllers.*;

import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import models.Event;
import models.User;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.HomeUI;
import views.MasterUI;

/**
 * After the user has successfully created an event, they are sent
 * to this panel on which they see a success message.
 */
public class ScheduleEventConfirm extends Panel {

  private static final long serialVersionUID = 4625075272906690489L;
  private ScheduleEvent createMeetingPanel;

  public ScheduleEventConfirm(JFrame frame, User user, Event event, int mode) {
    super(frame);
    
    Button backCreate;
    createMeetingPanel = new ScheduleEvent(frame, user, null, ScheduleModes.CREATE);
    if (mode == ScheduleModes.CREATE) {
      backCreate = new Button(40, 400, "Back to creator", createMeetingPanel);
    } else {
      backCreate = new Button(40, 400, "Back to dashboard", HomeUI.dashPanel);
    }
    backCreate.setIcon(MasterUI.backIcon);
    backCreate.setColor(MasterUI.secondaryCol);
    backCreate.setVerticalAlignment(SwingConstants.CENTER);
    backCreate.setVerticalTextPosition(SwingConstants.CENTER);
    backCreate.addActionListener(e -> HomeUI.createTab.changeReferencePanel(createMeetingPanel));
    backCreate.setCornerRadius(Button.ROUND);

    Label screenTitle = new Label(40, 40, "");
    Label successMsg = new Label(40, 150, "");
    Label secondaryMsg = new Label(40, 290, "All participants have been notified.");
    ImageIcon[] heroImages = { MasterUI.createdMeetingImage1, MasterUI.createdMeetingImage2 };
    Label heroImage = new Label(FormatUtil.resizeImageIcon(heroImages[new Random().nextInt(2)], 0.7f));
    heroImage.setSize(heroImage.getIcon().getIconWidth(), heroImage.getIcon().getIconHeight());

    String meetingDateDay = FormatUtil.formatOrdinal(event.getDate().getDayOfMonth());
    String meetingDateMonth = FormatUtil.capitalize(event.getDate().getMonth().toString());
    if (mode == ScheduleModes.CREATE) {
      successMsg.setText("<html>You have scheduled an event<br/>" + event.getName() + " for the " + meetingDateDay
          + " of " + meetingDateMonth + ".<html>");
      screenTitle.setText("Created.");
    } else if (mode == ScheduleModes.EDIT) {
      successMsg.setText("<html>Changed event info on<br/>" + event.getName() + " for the " + meetingDateDay
          + " of " + meetingDateMonth + ".<html>");
      screenTitle.setText("Edited.");
    }
    secondaryMsg.setSize(800, 40);
    heroImage.setLocation(400, 250);
    if (event.getParticipants().size() < 2) {
      secondaryMsg.setText("Your personal activity has been added.");
    }

    add(heroImage);
    add(backCreate);
    add(screenTitle);
    add(successMsg);
    add(secondaryMsg);
    MasterUI.setComponentStyles(this, "light");

    screenTitle.setHeading();
    screenTitle.setForeground(MasterUI.accentCol);
    successMsg.setHeading();
    successMsg.setSize(690, 120);

    ViewModelHandler.updateDashboard(user);
    ViewModelHandler.updateCalendar(frame);
  }
}
