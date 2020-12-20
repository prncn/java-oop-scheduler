import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Component;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class RegisterGUI extends LoginGUI {
  private static final long serialVersionUID = -6864343981907015773L;
  private JPasswordField passConfField;
  private JTextField emailField;
  private JLabel passConfLabel;
  private JLabel emailLabel;
  

  public RegisterGUI() {
    this.setTitle("Scheduler Sign-Up");
    this.setSize(600, 700);

    loginBtn.setText("Sign Up");
    registerBtn.setText("Go Back");
    
    passConfField = new JPasswordField();
    emailField = new JTextField();
    passConfLabel = createLabel(lgnBox.x, lgnBox.y + 170, "Confirm Password");
    emailLabel = createLabel(lgnBox.x, lgnBox.y + 240, "Email");
    backIconHero.setIcon(signupHeroImage);
    backIconHero.setBounds(250, 300, 400, 400);

    loginBtn.setBounds(lgnBox.x, lgnBox.y + 350, 100, 40);
    registerBtn.setBounds(lgnBox.x + 110, lgnBox.y + 350, 100, 40);
    passConfField.setBounds(lgnBox.x, lgnBox.y + 190, 210, 40);
    emailField.setBounds(lgnBox.x, lgnBox.y + 260, 210, 40);
    success.setBounds(lgnBox.x, lgnBox.y + 430, 250, 25);

    for(ActionListener al : loginBtn.getActionListeners()){
      loginBtn.removeActionListener(al);
    }
    for(ActionListener al : registerBtn.getActionListeners()){
      registerBtn.removeActionListener(al);
    }

    loginBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputUser = userField.getText();
        String inputPass = String.valueOf(passField.getPassword());
        String inputPassConf = String.valueOf(passConfField.getPassword());
        String inputEmail = emailField.getText();

        if(inputUser.isEmpty() || inputPass.isEmpty() || inputEmail.isEmpty()){
          success.setText("Required fields missing");
          return;
        } else if(!inputPassConf.equals(inputPass)){
          success.setText("Passwords don't match");
          return;
        } else if(!(inputEmail.contains("@") && inputEmail.contains("."))){
          success.setText("Invalid email");
          return;
        }
        
        String encryptPass = PasswordEncryption.createHash(inputPass);
        System.out.println(encryptPass);
        
        UserAccount user = new UserAccount(inputUser, encryptPass, inputEmail);
        if(!DataBaseAPI.isAvailable(user)){
          success.setText("User already exists");
          return;
        }
        DataBaseAPI.createUser(user);
        panel.removeAll();
        dispose();
        LoginGUI login = new LoginGUI();
        login.setVisible(true);
        success.setText("Account created");
      }
    });
    
    registerBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.removeAll();
        dispose();
        LoginGUI login = new LoginGUI();
        login.setVisible(true);
      }
    });

    panel.add(passConfLabel);
    panel.add(passConfField);
    panel.add(emailLabel);
    panel.add(emailField);

    setComponentStyles();
    screenTitle.setText("Create Account");
    screenTitle.setFont(bodyFont.deriveFont(Font.BOLD, 20f));

    this.setLocationRelativeTo(null);
  }
  
  public static void main(String[] args) {
    RegisterGUI register = new RegisterGUI();
    register.setVisible(true);
  }
}
