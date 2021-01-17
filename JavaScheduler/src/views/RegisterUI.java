package views;

import models.*;
import views.components.Label;
import views.components.TextField;
import controllers.*;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterUI extends LoginUI {
  private static final long serialVersionUID = -6864343981907015773L;
  private JPasswordField passConfField;
  private TextField emailField;
  private TextField firstnameField;
  private TextField lastnameField;

  private String inputUser;
  private String inputPass;
  private String inputPassConf;
  private String inputEmail;

  /** Margin between textfield label and textfield */
  private final int LBL_MRGN = 20;

  public RegisterUI() {
    setTitle("Scheduler Sign Up");
    setSize(600, 700);
    panel.updateBounds(this);

    loginBtn.setText("Sign Up");
    registerBtn.setText("Go Back");

    passConfField = new JPasswordField();
    emailField = new TextField(0, 0, "");
    firstnameField = new TextField(0, 0, "");
    lastnameField = new TextField(0, 0, "");
    backIconHero.setIcon(signupHeroImage);
    backIconHero.setBounds(250, 300, 400, 400);
    loginBtn.setBounds(lgnBox.x, lgnBox.y + 350, 100, 40);
    registerBtn.setBounds(lgnBox.x + 110, lgnBox.y + 350, 100, 40);

    placeToRight(passField, passConfField);
    placeFieldLabel(passConfField, "Confirm Password");
    firstnameField.setBounds(passField.getX(), passField.getY() + 70, passField.getWidth(), passField.getHeight());
    placeToRight(firstnameField, lastnameField);
    placeFieldLabel(firstnameField, "First name");
    placeFieldLabel(lastnameField, "Last name");
    emailField.setBounds(firstnameField.getX(), firstnameField.getY() + 70, passField.getWidth(),
        passField.getHeight());
    placeFieldLabel(emailField, "Email");
    success.setBounds(lgnBox.x, lgnBox.y + 430, 250, 25);
    setButtonActions();

    panel.add(passConfField);
    panel.add(emailField);
    panel.add(firstnameField);
    panel.add(lastnameField);

    setComponentStyles(panel, null);
    screenTitle.setText("Create Account");
    screenTitle.setHeading();
    setLocationRelativeTo(null);
  }

  /**
   * Place a textfield to right of another textfield
   * 
   * @param origin - Reference textfield on the left
   * @param field  - Textfield to be placed on the right
   */
  private void placeToRight(JTextField origin, JTextField field) {
    field.setBounds(origin.getX() + origin.getWidth() + LBL_MRGN, origin.getY(), origin.getWidth(), origin.getHeight());
  }

  /**
   * Place label of name of textfield above that textfield.
   * 
   * @param field - Textfield of input
   * @param name  - Name corresponding of textfield
   */
  private void placeFieldLabel(JTextField field, String name) {
    Label label = new Label(field.getX(), field.getY() - LBL_MRGN, name);
    panel.add(label);
  }

  /**
   * Validate inputs of form textfields. Set error message
   * on label in case of missing fields.
   */
  private void validateForm() {
    inputUser = userField.getText();
    inputPass = String.valueOf(passField.getPassword());
    inputPassConf = String.valueOf(passConfField.getPassword());
    inputEmail = emailField.getText();

    if (inputUser.isEmpty() || inputPass.isEmpty() || inputEmail.isEmpty()) {
      success.setText("Required fields missing");
      return;
    } else if (!inputPassConf.equals(inputPass)) {
      success.setText("Passwords don't match");
      return;
    } else if (!(inputEmail.contains("@") && inputEmail.contains("."))) {
      success.setText("Invalid email");
      return;
    }
  }

  /**
   * Send password input to encryption and create user in database.
   * Finally, check if user already is available and confirm success.
   */
  private void processRegistration() {
    String encryptPass = PasswordEncryption.createHash(inputPass);
    User user = new User(inputUser, encryptPass, inputEmail);
    if (!DataBaseAPI.isAvailable(user)) {
      success.setText("User already exists");
      return;
    }
    DataBaseAPI.createUser(user);
    panel.removeAll();
    LoginUI login = new LoginUI();
    login.setVisible(true);
    success.setText("Account created");
    dispose();
  }

  /**
   * Wipe button action of inherited login frame and replace
   * them with account creation and redirect actions.
   */
  private void setButtonActions() {
    for (ActionListener al : loginBtn.getActionListeners()) {
      loginBtn.removeActionListener(al);
    }
    for (ActionListener al : registerBtn.getActionListeners()) {
      registerBtn.removeActionListener(al);
    }

    loginBtn.addActionListener(e -> {
      validateForm();
      processRegistration();
    });

    registerBtn.addActionListener(e -> {
      panel.removeAll();
      dispose();
      LoginUI login = new LoginUI();
      login.setVisible(true);
    });
  }

  public static void main(String[] args) {
    RegisterUI register = new RegisterUI();
    register.setVisible(true);
  }
}
