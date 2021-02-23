package views.panels;

import controllers.DatabaseAPI;
import controllers.FormatUtil;
import controllers.PasswordEncryption;
import controllers.ViewModelHandler;
import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Profile info panel display account information.
 * This is used by the profile panel and the admin panel to
 * show user info.
 */
public class ProfilePanelInfo extends Panel {

  private static final long serialVersionUID = 1L;
  private TextField usernameField;
  private TextField passField;
  private TextField emailField;
  private TextField firstnameField;
  private TextField lastnameField;
  private List<JTextField> fields;
  private Button saveBtn;
  private User user;
  private boolean isEditable;

  public ProfilePanelInfo(User user, boolean isEditable) {
    super();
    this.user = user;
    setBackground(MasterUI.lightCol);
    setBounds(40, 100, 320, 550);

    usernameField = new TextField(user.getUsername());
    passField = new TextField("");
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

    fields = new ArrayList<>(Arrays.asList(usernameField, passField, emailField, firstnameField, lastnameField));

    Label profileIcon = new Label(0, 0, "");
    profileIcon.fillIcon(FormatUtil.resizeImageIcon(user.getAvatar(), 0.5f));
    Button piBtn = new Button(0, 0, "");
    piBtn.setSize(profileIcon.getSize());
    piBtn.setBlank(true);
    profileIcon.add(piBtn);
    piBtn.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.setFileFilter(new FileFilter() {
        public String getDescription() {
          return "Image Files";
        }

        public boolean accept(File f) {
          if (f.isDirectory()) {
            return true;
          } else {
            String filename = f.getName().toLowerCase();
            return filename.endsWith(".jpg") || filename.endsWith(".jpeg")
                || filename.endsWith(".png");
          }
        }
      });
      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        File img = chooser.getSelectedFile();
        byte[] bytes = FormatUtil.fileToBytes(img);
        user.setAvatar(FormatUtil.byteToIcon(bytes));
        profileIcon.fillIcon(FormatUtil.resizeImageIcon(user.getAvatar(), 0.5f));
      }
    });
    add(profileIcon);

    /** Content box */
    Point contentPoint = new Point(0, 100);
    Point cb = new Point(contentPoint.x, contentPoint.y);
    /** Vertical margin between text fields **/
    int marginBottom = 80;

    cb.setLocation(contentPoint.x, contentPoint.y);
    usernameField.setLocation(cb.x, cb.y - marginBottom);
    usernameField.setBounds(cb.x + marginBottom, usernameField.getY(), usernameField.getWidth() - marginBottom, usernameField.getHeight());
    passField.setLocation(cb.x, usernameField.getY() + marginBottom);
    emailField.setLocation(cb.x, passField.getY() + marginBottom);
    firstnameField.setLocation(cb.x, emailField.getY() + marginBottom);
    firstnameField.setSize(firstnameField.getWidth() / 2 - 5, firstnameField.getHeight());
    lastnameField.setLocation(firstnameField.getX() + firstnameField.getWidth() + 10, firstnameField.getY());
    lastnameField.setSize(firstnameField.getSize());
    fields.forEach(e -> add(e));

    MasterUI.placeFieldLabel(usernameField, "Username", this);
    MasterUI.placeFieldLabel(passField, "Password", this);
    MasterUI.placeFieldLabel(emailField, "Email", this);
    MasterUI.placeFieldLabel(firstnameField, "First name", this);
    MasterUI.placeFieldLabel(lastnameField, "Last name", this);
    MasterUI.setComponentStyles(this, "light");

    if (isEditable) {
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
   */
  public void setToStaticMode() {
    if (!getEditable())
      return;
    for (JTextField field : fields) {
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
   */
  public void setToEditMode() {
    if (getEditable())
      return;
    for (JTextField field : fields) {
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
   * Set user attributes to data from form. This replaces and overwrites current
   * user data.
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
        if (!passField.getText().isBlank()) {
          user.setPassword(PasswordEncryption.createHash(passField.getText()));
        }
        DatabaseAPI.editUser(user);
        ViewModelHandler.updateProfileIcon(user);
      }
    };
  }

  /**
   * Get save user data info button
   * 
   * @return Save Button object
   */
  public Button getSaveBtn() {
    return saveBtn;
  }

}
