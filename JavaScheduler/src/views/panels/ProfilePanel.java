package views.panels;

import models.User;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

public class ProfilePanel extends Panel {
  
  private static final long serialVersionUID = -3221137395347263706L;

  public ProfilePanel(JFrame frame, User user) {
    super(frame);

    Label profileTitle = new Label(40, 40, "Profile");
    profileTitle.setHeading();

    Label locationTitle = new Label(450, 40, "Custom Locations");
    locationTitle.setHeading();
    
    ProfilePanelInfo infoPanel = new ProfilePanelInfo(user);

    Button editBtn = new Button(140, 35, "Edit", MasterUI.lightCol);
    editBtn.addActionListener(infoPanel.setEditingAction());
    add(editBtn);

    Panel lcpanel = new Panel();
    lcpanel.setBackground(MasterUI.lightCol);
    lcpanel.setBounds(450, 100, 300, 500);
    add(lcpanel);

    Label lcnameLabel = new Label(0, 0, "Location name");
    TextField lcNameField = new TextField(0, 20); 
    lcpanel.add(lcnameLabel);
    lcpanel.add(lcNameField);

    TextField lcCityField = new TextField("");
    TextField lcZipField = new TextField("");
    TextField lcStreetField = new TextField("");
    TextField lcStreetNrField = new TextField("");
    TextField lcBuildingField = new TextField("");
    TextField lcRoomField = new TextField("");
    TextField[] lcFields = {lcCityField, lcZipField, lcStreetField, lcStreetNrField, lcBuildingField, lcRoomField};

    String[] lcLabels = {"City", "ZIP code", "Street", "Street Nr.", "Building", "Room Nr."};
    int y = 80;
    for(int i=0; i<lcLabels.length; i++) {
      String labelStr = lcLabels[i];
      TextField field = lcFields[i];
      int mid = field.getWidth() / 2 - 5;
      field.setSize(mid, field.getHeight());

      if(i % 2 == 0){
        Label label = new Label(0, y, labelStr);
        field.setPosition(0, y + 20);
        lcpanel.add(field);
        lcpanel.add(label);
      } else {
        Label label = new Label(mid + 10, y, labelStr);
        field.setPosition(mid + 10, y + 20);
        lcpanel.add(field);
        lcpanel.add(label);
        y += 80;
      }
    }

    add(infoPanel);
    add(profileTitle);
    add(locationTitle);
  }
}