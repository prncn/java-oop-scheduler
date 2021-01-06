package views.panels;

import models.Location;
import models.User;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

public class ProfilePanel extends Panel {
  
  private static final long serialVersionUID = -3221137395347263706L;
  private User user;

  public ProfilePanel(JFrame frame, User user) {
    super(frame);
    this.user = user;

    drawProfileSection(user);
    drawLocationSection();
  }

  /**
   * Wrapper for profile section
   * @param user - User to be display
   */
  private void drawProfileSection(User user) {
    Label profileTitle = new Label(40, 40, "Profile");
    profileTitle.setHeading();

    ProfilePanelInfo infoPanel = new ProfilePanelInfo(user);
    Button editBtn = new Button(140, 35, "Edit");
    editBtn.setDark(false);
    editBtn.setColor(MasterUI.lightCol);
    editBtn.addActionListener(infoPanel.setEditingAction());

    Button saveBtn = new Button(40, 430, "Save Changes", MasterUI.secondaryCol);
    saveBtn.setSize(300, 50);

    add(saveBtn);
    add(profileTitle);
    add(infoPanel);
    add(editBtn);

    MasterUI.setComponentStyles(this, "light");
  }

  /**
   * Wrapper for location section
   */
  private void drawLocationSection() {
    Label locationTitle = new Label(450, 40, "Custom Locations");
    locationTitle.setHeading();

    Panel lcpanel = new Panel();
    lcpanel.setBackground(MasterUI.lightCol);
    lcpanel.setBounds(450, 100, 300, 520);
    
    Label lcnameLabel = new Label(0, 0, "Location name");
    TextField lcNameField = new TextField(0, 20); 
    lcpanel.add(lcnameLabel);
    lcpanel.add(lcNameField);
    
    TextField[] lcFields = createLocationFields();
    drawLocationForm(lcFields, lcpanel);

    Panel lclistpanel = new Panel();
    lclistpanel.setBounds(0, lcpanel.getHeight() - 200, lcpanel.getWidth(), 200);
    lclistpanel.setBackground(Color.RED);
    lcpanel.add(lclistpanel);

    Location lc_1 = new Location("Campus", "Frankfurt", "60333", "Werner-Schilling-Strasse", "12", "", "");
    Location lc_2 = new Location("Park", "Lothric", "60333", "", "", "", "");
    Location lc_3 = new Location("Department", "", "90210", "", "", "Conference", "");

    user.addLocation(lc_1);
    user.addLocation(lc_2);
    user.addLocation(lc_3);

    List<Location> locations = user.getLocations();
    int y = 0;
    int lcBtnHght = 40;
    for(Location location : locations) {
      Button lcBtn = new Button(0, y, location.getName(), MasterUI.lightColAlt);
      lcBtn.setSize(lclistpanel.getWidth(), lcBtnHght);
      lcBtn.setDark(false);
      lcBtn.setHorizontalAlignment(SwingConstants.LEFT);
      lclistpanel.add(lcBtn);
      y += lcBtnHght;
    }
    
    MasterUI.setComponentStyles(lclistpanel, "light");
    add(lcpanel);
    add(locationTitle);
  }

  /**
   * Create and initialise text fields for location form
   * @return
   */
  private TextField[] createLocationFields() {
    TextField lcCityField = new TextField("");
    TextField lcZipField = new TextField("");
    TextField lcStreetField = new TextField("");
    TextField lcStreetNrField = new TextField("");
    TextField lcBuildingField = new TextField("");
    TextField lcRoomField = new TextField("");
    TextField[] lcFields = {lcCityField, lcZipField, lcStreetField, lcStreetNrField, lcBuildingField, lcRoomField};
    return lcFields;
  }

  /**
   * Draw form for location data
   * @param lcFields - Text fields for location form
   * @param lcpanel - Panel of location section
   */
  private void drawLocationForm(TextField[] lcFields, Panel lcpanel) {
    String[] lcLabels = {"City", "ZIP code", "Street", "Street Nr.", "Building", "Room Nr."};
    int y = 80;
    for(int i=0; i<lcLabels.length; i++) {
      String labelStr = lcLabels[i];
      TextField field = lcFields[i];
      int mid = field.getWidth() / 2 - 5;
      field.setSize(mid, field.getHeight());
      field.setName(labelStr);

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
  }
}