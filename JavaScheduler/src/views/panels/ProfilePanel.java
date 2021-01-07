package views.panels;

import models.Location;
import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

public class ProfilePanel extends Panel {

  private static final long serialVersionUID = -3221137395347263706L;
  private TextField lcNameField;
  private TextField lcCityField;
  private TextField lcZipField;
  private TextField lcStreetField;
  private TextField lcStreetNrField;
  private TextField lcBuildingField;
  private TextField lcRoomField;

  private Button editBtn;
  private Button cnclBtn;

  private Button lcSaveBtn;
  private Button lcClrBtn;
  private Button lcAddBtn;

  private Panel lclistpanel;

  private User user;

  public ProfilePanel(JFrame frame, User user) {
    super(frame);
    this.user = user;

    drawProfileSection(user);
    drawLocationSection();
  }

  /**
   * Wrapper for profile section
   * 
   * @param user - User to be display
   */
  private void drawProfileSection(User user) {
    Label profileTitle = new Label(40, 40, "Profile");
    profileTitle.setHeading();

    ProfilePanelInfo infoPanel = new ProfilePanelInfo(user, false);
    infoPanel.setStatic();

    editBtn = new Button(150, 35, "Edit");
    editBtn.setSmall();
    editBtn.addActionListener(infoPanel.setEditAction());

    cnclBtn = new Button(editBtn.getX() + editBtn.getWidth(), editBtn.getY(), "Cancel");
    cnclBtn.setSmall();
    cnclBtn.addActionListener(infoPanel.setStaticAction());

    add(editBtn);
    add(cnclBtn);
    add(profileTitle);
    add(infoPanel);

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

    Label lcNameLabel = new Label(0, 0, "Location name");
    lcNameField = new TextField(0, 20);
    lcpanel.add(lcNameLabel);
    lcpanel.add(lcNameField);

    TextField[] lcFields = initLocationFields();
    drawLocationForm(lcFields, lcpanel);

    Location lc_1 = new Location("Campus", "Frankfurt", "60333", "Werner-Schilling-Strasse", "12", "", "");
    Location lc_2 = new Location("Park", "Lothric", "60333", "", "", "", "");
    Location lc_3 = new Location("Department", "", "90210", "", "", "Conference", "");

    user.addLocation(lc_1);
    user.addLocation(lc_2);
    user.addLocation(lc_3);

    lcAddBtn = new Button(730, 35, "Add");
    lcAddBtn.setSmall();
    lcAddBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Location temp = new Location(lcNameField.getText());
        overwriteLocationData(temp);
        user.addLocation(temp);
        drawLocationListPanel(lcpanel);
      }
    });
    add(lcAddBtn);
    setComponentZOrder(lcAddBtn, 0);

    drawLocationListPanel(lcpanel);

    MasterUI.setComponentStyles(this, "light");
    add(lcpanel);
    add(locationTitle);
  }

  /**
   * Draw list panel of locations of user
   * 
   * @param lcpanel - Location panel for the list panel to be placed on
   */
  private void drawLocationListPanel(Panel lcpanel) {
    if (lclistpanel != null) {
      lcpanel.remove(lclistpanel);
    }
    lclistpanel = new Panel();
    lclistpanel.setBounds(0, lcpanel.getHeight() - 200, lcpanel.getWidth(), 200);
    lclistpanel.setBackground(MasterUI.lightCol);
    lcpanel.add(lclistpanel);

    List<Location> locations = user.getLocations();
    int y = 0;
    int lcBtnHght = 40;
    for (Location location : locations) {
      Button lcBtn = new Button(0, y, location.getName(), MasterUI.lightColAlt);
      lcBtn.setSize(lclistpanel.getWidth(), lcBtnHght);
      lcBtn.setDark(false);
      lcBtn.setHorizontalAlignment(SwingConstants.LEFT);
      lcBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (lcSaveBtn != null || lcClrBtn != null) {
            remove(lcSaveBtn);
            remove(lcClrBtn);
          }
          sendLocationForm(location);
          addEditLocationButtons(location);
        }
      });
      lclistpanel.add(lcBtn);
      y += lcBtnHght;
    }
    MasterUI.setComponentStyles(lclistpanel, "light");
    repaint();
  }

  /**
   * Remove location save and clear buttons.
   */
  private void removeEditLocationButtons() {
    remove(lcSaveBtn);
    remove(lcClrBtn);
    add(lcAddBtn);
    setComponentZOrder(lcAddBtn, 0);
    repaint();
  }

  /**
   * Create and initialise edit and add button for location section. If a location
   * is selected, the user can edit that selection. If none are selected an option
   * to add a new entry appears.
   */
  private void addEditLocationButtons(Location location) {
    if (lcAddBtn != null) {
      remove(lcAddBtn);
    }
    lcSaveBtn = new Button(730, 35, "Save");
    lcSaveBtn.setSmall();
    lcSaveBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HomeUI.confirmDialog(saveFormLocationData(location), "Replace location info?");
      }
    });
    lcClrBtn = new Button(lcSaveBtn.getX() + lcSaveBtn.getWidth(), lcSaveBtn.getY(), "Clear");
    lcClrBtn.setSmall();
    lcClrBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        clearLocationForm();
        removeEditLocationButtons();
      }
    });

    add(lcSaveBtn);
    add(lcClrBtn);
    MasterUI.setComponentStyles(this, "light");

    setComponentZOrder(lcSaveBtn, 0);
    setComponentZOrder(lcClrBtn, 1);

    repaint();
  }

  /**
   * Parse location object data into form
   * 
   * @param location - Given location object
   */
  public void sendLocationForm(Location location) {
    lcNameField.setText(location.getName());
    lcCityField.setText(location.getCity());
    lcZipField.setText(location.getZip());
    lcStreetField.setText(location.getStreet());
    lcStreetNrField.setText(location.getStreetNr());
    lcBuildingField.setText(location.getBuilding());
    lcRoomField.setText(location.getRoomNr());
  }

  /**
   * Set location attributes to data from location form. This overwrites and
   * replaces current location data.
   */
  public ActionListener saveFormLocationData(Location location) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        overwriteLocationData(location);
        removeEditLocationButtons();
      }
    };
  }

  /**
   * Overwrite location attribute of given location.
   * 
   * @param location - Location object
   */
  public void overwriteLocationData(Location location) {
    location.setName(lcNameField.getText());
    location.setCity(lcCityField.getText());
    location.setZip(lcZipField.getText());
    location.setStreet(lcStreetField.getText());
    location.setStreetNr(lcStreetNrField.getText());
    location.setBuilding(lcBuildingField.getText());
    location.setRoomNr(lcRoomField.getText());
  }

  /**
   * Clear text fields of location form
   */
  public void clearLocationForm() {
    lcNameField.setText("");
    lcCityField.setText("");
    lcZipField.setText("");
    lcStreetField.setText("");
    lcStreetNrField.setText("");
    lcBuildingField.setText("");
    lcRoomField.setText("");
  }

  /**
   * Create and initialise text fields for location form
   * 
   * @return
   */
  private TextField[] initLocationFields() {
    lcCityField = new TextField("");
    lcZipField = new TextField("");
    lcStreetField = new TextField("");
    lcStreetNrField = new TextField("");
    lcBuildingField = new TextField("");
    lcRoomField = new TextField("");
    TextField[] lcFields = { lcCityField, lcZipField, lcStreetField, lcStreetNrField, lcBuildingField, lcRoomField };
    return lcFields;
  }

  /**
   * Draw form for location data
   * 
   * @param lcFields - Text fields for location form
   * @param lcpanel  - Panel of location section
   */
  private void drawLocationForm(TextField[] lcFields, Panel lcpanel) {
    String[] lcLabels = { "City", "ZIP code", "Street", "Street Nr.", "Building", "Room Nr." };
    int y = 80;
    for (int i = 0; i < lcLabels.length; i++) {
      String labelStr = lcLabels[i];
      TextField field = lcFields[i];
      int mid = field.getWidth() / 2 - 5;
      field.setSize(mid, field.getHeight());
      field.setName(labelStr);

      if (i % 2 == 0) {
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