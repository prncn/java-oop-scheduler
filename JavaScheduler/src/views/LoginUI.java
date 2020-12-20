package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;

import javax.swing.*;

import controllers.DataBaseAPI;
import views.components.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class LoginUI extends MasterUI {
  private static final long serialVersionUID = -6864342302747015773L;

  /**
   * UI components
   */
  private static Label userLabel;
  protected static JTextField userField;
  private static Label passLabel;
  protected static JPasswordField passField;
  protected static Button loginBtn;
  protected static Button registerBtn;
  protected static Label success;
  protected JLabel backIconHero;
  protected Label screenTitle;
  protected static Point lgnBox = new Point(100, 70);

  // protected static JPanel panel = new JPanel();

  public LoginUI() {
    this.setTitle("Scheduler Login");
    this.setSize(600, 500);

    userLabel = new Label(lgnBox.x, lgnBox.y + 30, "Username");
    passLabel = new Label(lgnBox.x, lgnBox.y + 100, "Password");
    userField = new JTextField();
    passField = new JPasswordField();
    loginBtn = new Button(lgnBox.x, lgnBox.y + 180, "Login", accentCol);
    registerBtn = new Button(lgnBox.x + 110, lgnBox.y + 180, "Sign Up");
    success = new Label(lgnBox.x, lgnBox.y + 250, "");
    backIconHero = new JLabel(loginHeroImage);
    screenTitle = new Label(lgnBox.x, lgnBox.y, "Login to Your Account");

    backIconHero.setBounds(200, 250, 400, 400);

    userField.setBounds(lgnBox.x, lgnBox.y + 50, 210, 40);
    passField.setBounds(lgnBox.x, lgnBox.y + 120, 210, 40);

    this.getRootPane().setDefaultButton(loginBtn);

    loginBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputUser = userField.getText();
        String inputPass = String.valueOf(passField.getPassword());

        if(inputUser.isEmpty() || inputPass.isEmpty()){
          System.out.println("Either input empty..");
          return;
        }

        try {
          if (DataBaseAPI.verifyUser(inputUser, inputPass)) {
            dispose();
            userWelcome.setText("Hi, " + inputUser + "!");
            HomeUI home = new HomeUI();
            home.setVisible(true);
          } else {
            success.setText("Wrong username or password");
            passField.setText("");
          }
        } catch (SQLException sqlException) {
          sqlException.printStackTrace();
        }
      }
    });

    registerBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.removeAll();
        dispose();
        RegisterUI register = new RegisterUI();
        register.setVisible(true);
      }
    });

    panel.add(userLabel);
    panel.add(passLabel);
    panel.add(userField);
    panel.add(passField);
    panel.add(loginBtn);
    panel.add(registerBtn);
    panel.add(success);
    panel.add(backIconHero);
    panel.add(screenTitle);

    setComponentStyles();
    screenTitle.setTitle();
    this.setLocationRelativeTo(null);
  }

  public static void main(String[] args) {
    LoginUI logFrame = new LoginUI();
    logFrame.setVisible(true);
  }

  /**
   * Class to make borderless window draggable (Unused if native windows borders
   * are used).
   */
  public static class WindowDragAction extends MouseAdapter {
    private final JFrame frame;
    private Point mouseDownCompCoords = null;

    public WindowDragAction(JFrame frame) {
      this.frame = frame;
    }
    public void mouseReleased(MouseEvent e) {
      mouseDownCompCoords = null;
    }
    public void mousePressed(MouseEvent e) {
      mouseDownCompCoords = e.getPoint();
    }
    public void mouseDragged(MouseEvent e) {
      Point currCoords = e.getLocationOnScreen();
      frame.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
    }
  }
}
