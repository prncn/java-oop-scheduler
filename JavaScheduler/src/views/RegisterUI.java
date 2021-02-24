package views;

import models.*;
import views.components.TextField;
import controllers.*;
import java.awt.event.ActionListener;

import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Account sign up panel. User accounts are created
 * here.
 */
public class RegisterUI extends LoginUI {
  private static final long serialVersionUID = -6864343981907015773L;
  /**Textfield where password is entered*/
  private JPasswordField passConfField;
  /**Textfield where email is entered*/
  private TextField emailField;
  /**Textfield where firstname is entered*/
  private TextField firstnameField;
  /**Textfield where lastname is entered*/
  private TextField lastnameField;
  /**String containing the input for username*/
  private String inputUser;
  /**String containing the input for password*/
  private String inputPass;
  /**String containing the input for password confirmation*/
  private String inputPassConf;
  /**String containing the input for email*/
  private String inputEmail;
  /**String containing the input for firstname*/
  private String inputFirstname;
  /**String containing the input for lastname*/
  private String inputLastname;


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
    placeFieldLabel(passConfField, "Confirm Password", panel);
    firstnameField.setBounds(passField.getX(), passField.getY() + 70, passField.getWidth(), passField.getHeight());
    placeToRight(firstnameField, lastnameField);
    placeFieldLabel(firstnameField, "First name", panel);
    placeFieldLabel(lastnameField, "Last name", panel);
    emailField.setBounds(firstnameField.getX(), firstnameField.getY() + 70, passField.getWidth(),
        passField.getHeight());
    placeFieldLabel(emailField, "Email", panel);
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
   * Validate inputs of form textfields. Set error message
   * on label in case of missing fields.
   */
  private void validateForm() {
    inputUser = userField.getText();
    inputPass = String.valueOf(passField.getPassword());
    inputPassConf = String.valueOf(passConfField.getPassword());
    inputEmail = emailField.getText();
    inputFirstname = firstnameField.getText();
    inputLastname = lastnameField.getText();

    if (inputUser.isBlank() || inputPass.isBlank() || inputEmail.isBlank() || inputFirstname.isBlank() || inputLastname.isBlank()) {
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
    User user = new User(inputUser, encryptPass, inputFirstname, inputLastname, inputEmail);
    if (!DatabaseAPI.isAvailable(user)) {
      success.setText("User already exists");
      return;
    }
    DatabaseAPI.storeUser(user);
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
