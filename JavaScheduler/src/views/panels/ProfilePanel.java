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

  private User user;
  private static JFrame frame;

  public ProfilePanel(JFrame frame, User user) {
    super(frame);
    this.user = user;
    ProfilePanel.frame = frame;

    Label screenTitle = new Label(40, 40, "User Profile");
    screenTitle.setHeading();

    redpanel = createMainPanel(user, false);
    disableEditing();
    createEditButton();

    Label locationsTitle = new Label(500, 40, "Custom Locations");
    locationsTitle.setHeading();

    this.add(redpanel);
    this.add(screenTitle);
    this.add(locationsTitle);
    ((MasterUI) frame).setComponentStyles(this, "light");
  }

  /**
   * Initialise user data form
   * @param user - User
   */
  public static void initForm(User user) {
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
   * Create main profile data forms
   * @param user - User
   * @param panel - Panel on which to create main profile forms
   */
  public static Panel createMainPanel(User user, boolean editable) {
    Point cb = new Point(40, 100);
    Panel redpanel = new Panel();
    redpanel.setBackground(MasterUI.primaryColAlt);
    redpanel.setAlpha(0.95f);
    redpanel.setBounds(cb.x, cb.y, 390, 510);

    initForm(user);

    Label[] labels = {usernameLabel, emailLabel, firstnameLabel, lastnameLabel};
    TextField[] fields = {usernameField, emailField, firstnameField, lastnameField};
    int initY = 20;
    for(Label label : labels){
      label.setPosition(10, initY);
      label.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD));
      initY += 70;
      redpanel.add(label);
    }
    initY = 10;
    for(TextField field : fields){
      field.setSize(280, field.getHeight());
      field.setPosition(100, initY);
      initY += 70;
      redpanel.add(field);
    }

    if(editable){
      Button saveBtn = new Button (10, 450, "Save Changes", MasterUI.secondaryCol);
      saveBtn.setTab();
      saveBtn.centerText();
      saveBtn.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent e) {
          overwriteUserData(user);

        }
      });
      redpanel.add(saveBtn);
    }
    ((MasterUI) frame).setComponentStyles(redpanel, "dark");
    return redpanel;
  }

  /**
   * Overwrite user's data to input from text fields and save to DB
   */
  public static void overwriteUserData(User user){
    user.setUsername(usernameField.getText());
    user.setEmail(emailField.getText());
    user.setFirstname(firstnameField.getText());
    user.setLastname(lastnameField.getText());

    //DataBaseAPI.replaceUser(olduser, user);
  }

  /**
   * Make text fields uneditable
   */
  public void disableEditing() {
    TextField[] fields = {usernameField, emailField, firstnameField, lastnameField};
    for(TextField field : fields) {
      field.setEditable(false);
      field.wipeBackground();
      field.setEqualPadding(5);
    }
  }

  /**
   * Create user profile edit button. This unlocks info text fields.
   */
  public void createEditButton() {
    TextField[] fields = {usernameField, emailField, firstnameField, lastnameField};
    Button editbtn = new Button(230, 35, "Edit");
    editbtn.setBackground(MasterUI.lightCol);
    editbtn.setDark(false);
    Panel panel = this;
    editbtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for(TextField field : fields) {
          field.setEditable(true);
          field.setOpaque(true);
          field.setBackground(MasterUI.primaryCol);
        }
        panel.remove(redpanel);
        redpanel = createMainPanel(user, true);
        panel.add(redpanel);
        panel.repaint();
      }
    });
    this.add(editbtn);
  }
}
