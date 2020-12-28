package views;

import controllers.*;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import models.Meeting;
import models.UserAccount;
import views.components.Button;
import views.components.Label;
import views.components.Panel;

public class CreateMeetingConfirm extends Panel {

  private static final long serialVersionUID = 4625075272906690489L;

  public CreateMeetingConfirm(JFrame frame, UserAccount user, Meeting meeting) {
    super(frame);
    
    CreateMeetingPanel createMeetingPanel = new CreateMeetingPanel(frame, user);
    Button backCreate = new Button(40, 400, "Back to creator", createMeetingPanel);
    backCreate.setIcon(MasterUI.backIcon);
    backCreate.setColor(MasterUI.secondaryCol);
    backCreate.setVerticalAlignment(SwingConstants.CENTER);
    backCreate.setVerticalTextPosition(SwingConstants.BOTTOM);
    backCreate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HomeUI.createTab.changeReferencePanel(createMeetingPanel);
      }
    });
      
    Label screenTitle = new Label(40, 40, "Created.");
    Label successMsg = new Label(40, 150, "");
    Label secondaryMsg = new Label(40, 290, "All participants have been notified.");
    Label heroImage = new Label(MasterUI.createdMeetingImage);

    String meetingDateDay = Formatter.formatOrdinal(meeting.getEvent().getDate().getDayOfMonth());
    String meetingDateMonth = Formatter.capitalize(meeting.getEvent().getDate().getMonth().toString());
    successMsg.setText("<html>You have scheduled a meeting<br/>on " + meeting.getEvent().getName() 
    + " for the " + meetingDateDay + " of "+ meetingDateMonth + ".<html>");
    secondaryMsg.setSize(800, 40);
    heroImage.setBounds(400, 250, 542, 366);

    this.add(heroImage);
    this.add(backCreate);
    this.add(screenTitle);
    this.add(successMsg);
    this.add(secondaryMsg);
    ((MasterUI) frame).setComponentStyles(this, "light");

    screenTitle.setHeading();
    screenTitle.setForeground(MasterUI.accentCol);
    successMsg.setHeading();
    successMsg.setSize(690, 120);
  }
}
