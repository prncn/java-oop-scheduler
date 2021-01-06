package views.panels;

import models.User;
import views.MasterUI;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.Point;

public class ProfilePanelInfo extends Panel {

  private static final long serialVersionUID = 1L;
  private static TextField usernameField;
  private static TextField emailField;
  private static TextField firstnameField;
  private static TextField lastnameField;
  private boolean isEditable = true;
  
  public ProfilePanelInfo(User user) {
    super();
    setBackground(MasterUI.lightCol);
    setBounds(40, 100, 300, 500);

    String[] labels = {"Username", "Email", "First name", "Last name"};
    usernameField = new TextField(user.getUsername());
    emailField = new TextField(user.getEmail());
    firstnameField = new TextField(user.getFirstname());
    lastnameField = new TextField(user.getLastname());
    TextField[] fields = {usernameField, emailField, firstnameField, lastnameField};
    
    Point contentPoint = new Point(0, 0);
    Point cb = new Point(contentPoint.x, contentPoint.y); // Content box
    int marginBottom = 80; // Vertical margin between text fields
    for(String label : labels) {
      add(new Label(0, cb.y, label));
      cb.y += marginBottom;
    }
    
    cb.setLocation(contentPoint.x, contentPoint.y);
    for(TextField field : fields) {
      field.setLocation(cb.x, cb.y + 20);
      add(field);
      cb.y += marginBottom;
    }
    
    MasterUI.setComponentStyles(this, "light");
    setStatic();
  }

  // public User fetchUserData() {
  //   return new User(usernameField.getText(), emailField.getText(), )
  // }

  /**
   * Set given fields to static, making uneditable and read-only
   * @param fields - Text fields for user data
   */
  public void setStatic() {
    if(!getEditable()) return;
    TextField[] fields = {usernameField, emailField, firstnameField, lastnameField};
    for(TextField field : fields) {
      field.setBackground(MasterUI.lightCol);
      field.setEditable(false);
      field.setEqualPadding(0);
    }
    setEditable(false);
  }

  /**
   * Set given fields to editable mode
   * @param fields - Text fields for user data
   */
  public void setEdit() {
    if(getEditable()) return;
    TextField[] fields = {usernameField, emailField, firstnameField, lastnameField};
    for(TextField field : fields) {
      field.setBackground(MasterUI.lightColAlt);
      field.setEditable(true);
      field.setEqualPadding(5);
    }
    setEditable(true);
  }
  
  /**
   * Bind {@link #setEdit()} to action listener
   * @return ActionListener object
   */
  public ActionListener setEditingAction() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setEdit();
      }
    };
  }

  /**
   * Bind {@link #setStatic()} to action listener
   * @return ActionListener object
   */
  public ActionListener setStaticAction() {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        setStatic();
      }
    };
  }

  /**
   * Set wether form is in editing mode
   * @param value - Boolean isEditing value
   */
  private void setEditable(boolean value) {
    isEditable = value;
  }

  /**
   * Get wether form is in editing mode
   * @return Boolean isEditable value
   */
  private boolean getEditable() {
    return isEditable;
  }
}
