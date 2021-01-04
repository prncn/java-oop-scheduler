package views.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

import models.User;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

public class ProfilePanel extends Panel {

  private static final long serialVersionUID = 1L;

  private static Label usernameLabel;
  private static Label emailLabel;
  private static Label firstnameLabel;
  private static Label lastnameLabel;
  private static Panel redpanel;

  private static TextField usernameField;
  private static TextField emailField;
  private static TextField firstnameField;
  private static TextField lastnameField;

  private static Button saveBtn;
  private static Button cnclBtn;

  private User user;
  private static boolean isEditable;
  private static JFrame frame;

  public ProfilePanel(JFrame frame, User user) {
    super(frame);
    this.user = user;
    ProfilePanel.frame = frame;

    Label screenTitle = new Label(40, 40, "User Profile");
    screenTitle.setHeading();

    Panel panel = this;
    redpanel = createMainPanel(user, false, panel);
    createEditButton(panel, user);

    Label locationsTitle = new Label(500, 40, "Custom Locations");
    locationsTitle.setHeading();

    Panel locationPanel = new Panel();
    Point cb = new Point(40, 100);
    redpanel.setBounds(cb.x, cb.y, 390, 510);

    this.add(redpanel);
    this.add(screenTitle);
    this.add(locationsTitle);
    ((MasterUI) frame).setComponentStyles(this, "light");
  }

  /**
   * Initialise user data form
   * 
   * @param user - User of session
   */
  private static void initForm(User user) {
    usernameLabel = new Label("Username");
    emailLabel = new Label("Email");
    firstnameLabel = new Label("First name");
    lastnameLabel = new Label("Last name");

    usernameField = new TextField(user.getUsername());
    emailField = new TextField(user.getEmail());
    firstnameField = new TextField(user.getFirstname());
    lastnameField = new TextField(user.getLastname());
  }

  /**
   * Revoke edits and set fields to old values
   * 
   * @param user - User of session
   */
  private static void discardProfileChanges(User user) {
    usernameField.setText(user.getUsername());
    emailField.setText(user.getEmail());
    firstnameField.setText(user.getFirstname());
    lastnameField.setText(user.getLastname());
  }

  /**
   * Create main profile data forms
   * 
   * @param user  - User
   * @param panel - Panel on which to create main profile forms
   */
  public static Panel createMainPanel(User user, boolean editable, Panel panel) {
    Point cb = new Point(40, 100);
    redpanel = new Panel();
    setEditable(editable);
    redpanel.setBackground(MasterUI.primaryCol);
    redpanel.setBounds(cb.x, cb.y, 390, 510);

    initForm(user);
    Label[] labels = { usernameLabel, emailLabel, firstnameLabel, lastnameLabel };
    TextField[] fields = { usernameField, emailField, firstnameField, lastnameField };
    positionComponents(labels, fields);

    if (editable) {
      addProfileBtns(fields, user);
    } else {
      disableEditing(fields);
    }
    ((MasterUI) frame).setComponentStyles(redpanel, "dark");
    return redpanel;
  }

  /**
   * Add user profile save and cancel changes buttons and set their button actions
   * @param fields - Text fields for user profile data
   */
  private static void addProfileBtns(TextField[] fields, User user) {
    ActionListener save = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        overwriteUserData(user);
      }
    };

    ActionListener exit = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        disableEditing(fields);
        discardProfileChanges(user);
        removeProfileBtns();
        setEditable(false);
      }
    };

    saveBtn = new Button(10, 450, "Save Changes", MasterUI.secondaryCol);
    styleBtn(saveBtn, save);
    cnclBtn = new Button(saveBtn.getX() + saveBtn.getWidth() + 10, saveBtn.getY(), "Cancel");
    styleBtn(cnclBtn, exit);
    saveBtn.addActionListener(exit);

    redpanel.add(saveBtn);
    redpanel.add(cnclBtn);
    redpanel.repaint();
  }

  /**
   * Remove user profile save and cancel buttons
   */
  private static void removeProfileBtns() {
    redpanel.remove(saveBtn);
    redpanel.remove(cnclBtn);
    redpanel.repaint();
  }

  /**
   * Style save and cancal buttons
   * 
   * @param btn    - Button to be edited
   * @param action - ActionListener to be set
   */
  private static void styleBtn(Button btn, ActionListener action) {
    btn.setTab();
    btn.setSize(180, btn.getHeight());
    btn.centerText();
    btn.addActionListener(action);
  }

  /**
   * Position and style label and text field components
   */
  private static void positionComponents(Label[] labels, TextField[] fields) {
    int initY = 20;
    for (Label label : labels) {
      label.setPosition(10, initY);
      label.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD));
      initY += 60;
      redpanel.add(label);
    }
    initY = 10;
    for (TextField field : fields) {
      field.setSize(280, field.getHeight());
      field.setCaretColor(Color.WHITE);
      field.setPosition(100, initY);
      initY += 60;
      redpanel.add(field);
    }
  }

  /**
   * Overwrite user's data to input from text fields and save to DB
   */
  private static void overwriteUserData(User user) {
    user.setUsername(usernameField.getText());
    user.setEmail(emailField.getText());
    user.setFirstname(firstnameField.getText());
    user.setLastname(lastnameField.getText());

    // DataBaseAPI.replaceUser(olduser, user);
  }

  /**
   * Make text fields uneditable
   */
  private static void disableEditing(TextField[] fields) {
    for (TextField field : fields) {
      field.setEditable(false);
      field.wipeBackground();
      field.setEqualPadding(5);
    }
  }

  /**
   * Create user profile edit button. This unlocks info text fields.
   * @param panel
   * @param user
   */
  public static void createEditButton(Panel panel, User user) {
    TextField[] fields = { usernameField, emailField, firstnameField, lastnameField };
    Button editbtn = new Button(230, 35, "Edit");
    editbtn.setBackground(MasterUI.lightCol);
    editbtn.setDark(false);
    editbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if(isEditable) return;
        for (TextField field : fields) {
          field.setEditable(true);
          field.setOpaque(true);
          field.setBackground(MasterUI.primaryColAlt);
        }
        addProfileBtns(fields, user);
        setEditable(true);
      }
    });
    panel.add(editbtn);
  }

  /**
   * Get wether panel is editable
   * @return Boolean of isEditable
   */
  public static boolean getEditable() {
    return isEditable;
  }

  /**
   * Set wether panel is editable
   * @param value - Boolean for isEditable
   */
  public static void setEditable(boolean value) {
    isEditable = value;
  }
}
