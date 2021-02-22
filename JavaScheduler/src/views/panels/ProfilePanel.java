package views.panels;

import controllers.DatabaseAPI;
import models.Event;
import models.Location;
import models.User;
import views.HomeUI;
import views.LoginUI;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;
import views.components.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ProfilePanel extends Panel {

  private static final long serialVersionUID = -3221137395347263706L;
  private TextField lcNameField;
  private TextField lcCityField;
  private TextField lcZipField;
  private TextField lcStreetField;
  private TextField lcStreetNrField;
  private TextField lcBuildingField;
  private TextField lcRoomField;
  private TextField selectLocation;

  private Button editBtn;
  private Button cnclBtn;
  private Button deleteBtn;

  private Button lcSaveBtn;
  private Button lcClrBtn;
  private Button lcAddBtn;

  private static Label stats_1;
  private JScrollPane lcscroll;
  private Panel lcpanel;

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
    Panel panel = this;
    Label profileTitle = new Label(40, 40, "Profile");
    profileTitle.setHeading();

    ProfilePanelInfo infoPanel = new ProfilePanelInfo(user, false);
    infoPanel.setToStaticMode();

    deleteBtn = new Button(profileTitle.getX() + 200, profileTitle.getY(), "Delete Account", MasterUI.hiPrioCol);
    deleteBtn.setSmall();
    deleteBtn.setSize(deleteBtn.getWidth() + 50, deleteBtn.getHeight());
    deleteBtn.addActionListener(confirm -> HomeUI.confirmDialog(deleteUser(user), "Delete Account?"));
    add(deleteBtn);

    editBtn = new Button(40, 440, "Edit Profile", MasterUI.primaryCol);
    editBtn.setSize(310, 50);
    editBtn.setCornerRadius(Button.ROUND);
    editBtn.addActionListener(infoPanel.setEditAction());
    editBtn.addActionListener(e -> {
      remove(editBtn);
      remove(deleteBtn);
      add(cnclBtn);
      panel.setComponentZOrder(cnclBtn, 0);
      panel.repaint();
    });

    cnclBtn = new Button(profileTitle.getX() + 250, profileTitle.getY(), "Close");
    cnclBtn.setSmall();
    cnclBtn.addActionListener(infoPanel.setStaticAction());
    cnclBtn.addActionListener(e -> {
      remove(cnclBtn);
      add(deleteBtn);
      add(editBtn);
      panel.setComponentZOrder(editBtn, 0);
      panel.repaint();
    });

    infoPanel.getSaveBtn().addActionListener(e -> {
      remove(cnclBtn);
      add(editBtn);
      add(deleteBtn);
      panel.setComponentZOrder(editBtn, 0);
      panel.repaint();
    });

    stats_1 = new Label(40, 600, "You're partaking in " + user.getEvents().size() + " meetings.");
    stats_1.setForeground(MasterUI.accentCol);
    stats_1.setUnset(true);

    add(stats_1);
    add(editBtn);
    add(profileTitle);
    add(infoPanel);

    MasterUI.setComponentStyles(this, "light");
  }

  /**
   * Update current user profile statistics.
   */
  public static void updateProfileStats(User user) {
    stats_1.setText("You're partaking in " + user.getEvents().size() + " meetings.");
  }

  /**
   * Wrapper for location section
   */
  private void drawLocationSection() {
    Label locationTitle = new Label(450, 40, "Custom Locations");
    locationTitle.setHeading();

    lcpanel = new Panel();
    lcpanel.setBackground(MasterUI.lightCol);
    lcpanel.setBounds(450, 100, 310, 630);

    lcNameField = new TextField(0, 20);
    lcNameField.setText("Test");
    MasterUI.placeFieldLabel(lcNameField, "Location name", lcpanel);
    lcpanel.add(lcNameField);

    TextField[] lcFields = initLocationFields();
    drawLocationForm(lcFields, lcpanel);

    Location lc_1 = new Location("Campus", "Frankfurt", "60333", "Werner-Schilling-Strasse", "12", "", "");
    Location lc_2 = new Location("Park", "Lothric", "60333", "", "", "", "");
    Location lc_3 = new Location("Department", "", "90210", "", "", "Conference", "");

    user.addLocation(lc_1);
    user.addLocation(lc_2);
    user.addLocation(lc_3);

    lcAddBtn = new Button(0, 340, "Create New Location", MasterUI.secondaryCol);
    lcAddBtn.setSize(lcNameField.getWidth(), 50);
    lcAddBtn.setCornerRadius(Button.ROUND);
    lcAddBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (lcNameField.getText().isBlank())
          return;
        HomeUI.confirmDialog((write -> {
          Location temp = new Location(lcNameField.getText());
          overwriteLocationData(temp);
          user.addLocation(temp);
        }), "Add new location " + lcNameField.getText() + "?");
      }
    });
    lcSaveBtn = new Button(0, 340, "Save location info", MasterUI.accentCol);
    lcSaveBtn.setDark(true);
    lcSaveBtn.setSize(lcNameField.getWidth(), 50);
    lcSaveBtn.setCornerRadius(Button.ROUND);
    lcpanel.add(lcAddBtn);

    selectLocation = new TextField(0, lcpanel.getHeight() - 200);
    selectLocation.setText("Select location to edit");
    selectLocation.setEditable(false);
    MasterUI.placeFieldLabel(selectLocation, "Edit custom location", lcpanel);
    selectLocation.setSize(selectLocation.getWidth() - 40, selectLocation.getHeight());
    Button dpdwn = selectLocation.appendButton(MasterUI.downIcon);
    dpdwn.addActionListener(e -> drawLocationListPanel(lcpanel));

    lcClrBtn = new Button(730, 35, "Cancel");
    lcClrBtn.setSmall();

    lcpanel.add(dpdwn);
    lcpanel.add(selectLocation);

    lcClrBtn.addActionListener(e -> {
      removeEditLocationButtons();
      clearLocationForm();
    });

    add(lcpanel);
    add(locationTitle);
    MasterUI.setComponentStyles(lcpanel, "light");
    MasterUI.setComponentStyles(this, "light");
  }

  /**
   * Action for location options inside the location dropdown
   * 
   * @return ActionListener object
   */
  @SuppressWarnings("unchecked")
  private ActionListener lcBtnAction() {
    return (e -> {
      Location location = ((DataButton<Location>) e.getSource()).getData();
      addEditLocationButtons(location);
      sendLocationForm(location);
      selectLocation.setText(location.getName());
      lcpanel.remove(lcAddBtn);
      lcpanel.remove(lcscroll);
      lcpanel.repaint();
      lcscroll = null;
      for (ActionListener al : lcSaveBtn.getActionListeners())
        lcSaveBtn.removeActionListener(al);
      lcSaveBtn.addActionListener(btn -> {
        HomeUI.confirmDialog((write -> {
          overwriteLocationData(location);
          removeEditLocationButtons();
          clearLocationForm();
        }), "Replace location info?");
      });
    });
  }

  /**
   * User can delete his own account.
   * This include the deletion of all his events.
   * After deletion the HomeUI is closed and the LoginUI is opened
   * @param user
   * @return
   */
  public ActionListener deleteUser(User user) {
    return new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        /**
         * first get all events from user,
         * then deletes all user's hosted events (from list in events and in table Event),
         * then remove the user in all events where user is not the host (from the list in events)
         */
        ArrayList<Event> allEvents = DatabaseAPI.getEventsFromUser(user.getId());
        for (Event event : allEvents) {
          if(user.getId() == event.getHostId()) {
            user.deleteEvent(event);
          } else {
            event.removeParticipant(user);
          }
        }
        /**
         * delete user in database
         */
        DatabaseAPI.deleteUser(user.getId());
        HomeUI.disposeFrame();
        LoginUI login = new LoginUI();
        return;
      }
    };
  }

  /**
   * Draw list panel of locations of user. Per default the location lets the user
   * create a new location with the <code>add</code> button. If a existing
   * location is selected, the form is in editor mode.
   * 
   * @param lcpanel - Location panel for the list panel to be placed on
   */
  private void drawLocationListPanel(Panel lcpanel) {
    Component[] comps = selectLocation.setDropdown(user.getLocations(), lcscroll, lcpanel, lcBtnAction());
    lcscroll = (JScrollPane) comps[0];
    lcpanel = (Panel) comps[1];
  }

  /**
   * Remove location save and clear buttons. Push add button to foreground.
   */
  private void removeEditLocationButtons() {
    lcpanel.remove(lcSaveBtn);
    remove(lcClrBtn);
    lcpanel.add(lcAddBtn);
    lcpanel.repaint();
    repaint();
  }

  /**
   * Create and initialise edit and add button for location section. If a location
   * is selected, the user can edit that selection. If none are selected an option
   * to add a new entry appears.
   */
  private void addEditLocationButtons(Location location) {
    removeEditLocationButtons();
    lcpanel.add(lcSaveBtn);
    add(lcClrBtn);
    MasterUI.setComponentStyles(this, "light");
    MasterUI.setComponentStyles(lcpanel, "light");
    setComponentZOrder(lcClrBtn, 0);
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
      field.setName(labelStr);

      if (i % 2 == 0) {
        field.setSize(200, field.getHeight());
        field.setLocation(0, y + 20);
        MasterUI.placeFieldLabel(field, labelStr, lcpanel);
        lcpanel.add(field);
      } else {
        field.setSize(100, field.getHeight());
        field.setLocation(210, y + 20);
        MasterUI.placeFieldLabel(field, labelStr, lcpanel);
        lcpanel.add(field);
        y += 80;
      }
    }
  }
}