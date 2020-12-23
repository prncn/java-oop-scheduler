package views;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import controllers.DataBaseAPI;
import models.UserAccount;

import java.awt.Point;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import views.components.*;

public class createMeetingPanel extends Panel {

  private static final long serialVersionUID = 1L;
  private Label screenTitle;

  public createMeetingPanel(JFrame frame) {
    super(frame);
    this.setLayout(null);

    screenTitle = new Label(40, 40, "Schedule a Meeting");
    Point contentBox = new Point(40, 100);
    Button confirmBtn = new Button(40, 550, "Confirm", MasterUI.getColor("accentCol"));
    Button addUserBtn = new Button(710, 120, "Add User", new Color(102, 0, 255));
    confirmBtn.setTab();
    confirmBtn.setHorizontalAlignment(SwingConstants.CENTER);
    confirmBtn.setVerticalAlignment(SwingConstants.CENTER);
    
    String[] lbStrings = {"Title", "Date", "Duration", "Location"};
    initFormContent(contentBox, lbStrings);
    
    Label searchUserbl = new Label(400, 100, "Add Participant");
    TextField searchUser = new TextField(400, 120);

    Label userQueryResult = new Label(400, 170, "");
    addUserBtn.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String username = searchUser.getText();
        if(username.isEmpty()) return;
        UserAccount user = DataBaseAPI.getUser(username);
        if(user == null){
          userQueryResult.setText("User not found.");
        } else {
          userQueryResult.setText("Added " + username);
        }
      }
    });;

    
    this.add(screenTitle);
    this.add(searchUserbl);
    this.add(searchUser);
    this.add(confirmBtn);
    this.add(addUserBtn);
    this.add(userQueryResult);
    
    ((MasterUI) frame).setComponentStyles(this, "light");
    confirmBtn.setFont(MasterUI.monoFont);
    screenTitle.setHeading();
  }

  public void initFormContent(Point contentBox, String[] lbStrings) {
    // ArrayList<Label> lbList = new ArrayList<>();
    // ArrayList<TextField> tfList = new ArrayList<>();

    int initialY = contentBox.y;
    for(int i=0; i<lbStrings.length; i++){
      Label label = new Label(contentBox.x, initialY, lbStrings[i]);
      TextField textfield = new TextField(contentBox.x, initialY + 20);
      this.add(label);
      this.add(textfield);
      initialY += 70;
    }
  }

  public Label getScreenTitle() {
    return this.screenTitle;
  }
}
