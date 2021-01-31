package views.panels;

import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Button;
import views.components.Panel;
import views.components.TextField;

import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import controllers.DatabaseAPI;

import java.awt.event.ActionEvent;

import java.awt.Point;

public class ProfilePanelInfo extends Panel {

  private static final long serialVersionUID = 1L;
  private TextField usernameField;
  private TextField emailField;
  private TextField firstnameField;
  private TextField lastnameField;
  private Button saveBtn;
  private User user;
  private boolean isEditable;

  public ProfilePanelInfo(User user, boolean isEditable) {
    super();
    this.user = user;
    setBackground(MasterUI.lightCol);
    setBounds(40, 100, 320, 500);
    
    usernameField = new TextField(user.getUsername());
    emailField = new TextField(user.getEmail());
    firstnameField = new TextField(user.getFirstname());
    lastnameField = new TextField(user.getLastname());
    saveBtn = new Button(0, 340, "Save Changes", MasterUI.secondaryCol);
    saveBtn.setSize(310, 50);
    saveBtn.setCornerRadius(Button.ROUND);
    saveBtn.addActionListener(confirm -> HomeUI.confirmDialog(saveFormUserData(), e -> {
      resetForm();
    }, "Change profile info?"));
    saveBtn.addActionListener(e -> setToStaticMode());

    TextField[] fields = { usernameField, emailField, firstnameField, lastnameField };
    
    /** Content box */
    Point contentPoint = new Point(0, 0);
    Point cb = new Point(contentPoint.x, contentPoint.y); 
    /** Vertical margin between text fields **/
    int marginBottom = 80; 
    
    cb.setLocation(contentPoint.x, contentPoint.y);
    for (TextField field : fields) {
      field.setLocation(cb.x, cb.y + 20);
      add(field);
      cb.y += marginBottom;
    }
    
    MasterUI.placeFieldLabel(usernameField, "Username", this);
    MasterUI.placeFieldLabel(emailField, "Email", this);
    MasterUI.placeFieldLabel(firstnameField, "First name", this);
    MasterUI.placeFieldLabel(lastnameField, "Last name", this);
    MasterUI.setComponentStyles(this, "light");
    
    if(isEditable){
      this.isEditable = false;
      setToEditMode();
    } else {
      this.isEditable = true;
      setToStaticMode();
    }
    
    this.isEditable = isEditable;
  }

  /**
   * Set given fields to static, making uneditable and read-only
   * 
   * @param fields - Text fields for user data
   */
  public void setToStaticMode() {
    if (!getEditable())
      return;
    TextField[] fields = { usernameField, emailField, firstnameField, lastnameField };
    for (TextField field : fields) {
      field.setBackground(MasterUI.primaryCol);
      field.setForeground(MasterUI.lightCol);
      field.setEditable(false);
    }
    if (saveBtn != null) {
      remove(saveBtn);
    }

    repaint();
    setEditable(false);
  }

  /**
   * Set given fields to editable mode
   * 
   * @param fields - Text fields for user data
   */
  public void setToEditMode() {
    if (getEditable())
      return;
    TextField[] fields = { usernameField, emailField, firstnameField, lastnameField };
    for (TextField field : fields) {
      field.setBackground(MasterUI.lightColAlt);
      field.setEditable(true);
    }

    add(saveBtn);
    MasterUI.setComponentStyles(this, "light");
    repaint();

    setEditable(true);
  }

  /**
   * Bind {@link #setToEditMode()} to action listener
   * 
   * @return ActionListener object
   */
  public ActionListener setEditAction() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setToEditMode();
      }
    };
  }

  /**
   * Bind {@link #setToStaticMode()} to action listener
   * 
   * @return ActionListener object
   */
  public ActionListener setStaticAction() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setToStaticMode();
      }
    };
  }

  /**
   * Set whether form is in editing mode
   * 
   * @param value - Boolean isEditing value
   */
  private void setEditable(boolean value) {
    isEditable = value;
  }

  /**
   * Get whether form is in editing mode
   * 
   * @return Boolean isEditable value
   */
  private boolean getEditable() {
    return isEditable;
  }

  /**
   * Reset value of text fields to original user data
   */
  public void resetForm() {
    usernameField.setText(user.getUsername());
    emailField.setText(user.getEmail());
    firstnameField.setText(user.getFirstname());
    lastnameField.setText(user.getLastname());
  }

  /**
   * Set user attributes to data from form.
   * This replaces and overwrites current user data.
   * 
   * @return ActionListener object
   */
  public ActionListener saveFormUserData() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (usernameField.getText().isBlank() || emailField.getText().isBlank()) {
          HomeUI.confirmDialog("Username or mail cannot be blank.");
          resetForm();
          return;
        }
        User check = new User(user);
        check.setUsername(usernameField.getText());
        check.setEmail(emailField.getText());
        if (!DatabaseAPI.isAvailable(check)) {
          HomeUI.confirmDialog("Username or mail already exists.");
          resetForm();
          return;
        }
        user.setUsername(usernameField.getText());
        user.setEmail(emailField.getText());
        user.setFirstname(firstnameField.getText());
        user.setLastname(lastnameField.getText());
        DatabaseAPI.editUser(user);
      }
    };
  }

  /**
   * Get save user data info button
   * @return Save Button object
   */
  public Button getSaveBtn() {
    return saveBtn;
  }
}
