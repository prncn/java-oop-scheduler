package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Point;
import java.awt.Color;

import javax.swing.*;

import models.User;
import controllers.DatabaseAPI;
import views.components.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

/**
 * Login panel. This panel is booted on application startup
 * and sends the user to the login flow and finally the application.
 */
public class LoginUI extends MasterUI {
  private static final long serialVersionUID = -6864342302747015773L;

  /**
   * UI components
   */
  /**Textfield in which user can enter his username*/ 
  protected static TextField userField;
  /**Textfield in which user can enter his password*/
  protected static JPasswordField passField;
  /**button which is used to get to registration*/
  protected static Button loginBtn;
  /**button which is used to get to registration*/
  protected static Button registerBtn;
  /**shows whether login was successful*/
  protected static Label success;
  /**icon for the application*/
  protected JLabel backIconHero;
  /**contains the screentitle shown in LoginUI*/
  protected Label screenTitle;
  
  /** Pixel coordinates box for content */
  protected static Point lgnBox = new Point(70, 70);

  /** Margin between textfield label and textfield */
  protected final int LBL_MRGN = 20;


  public LoginUI() {
    setTitle("Scheduler Login");
    setSize(600, 500);
    panel.updateBounds(this);
    
    createLoginForm();
    registerBtnAction();
    loginBtnAction();
    getRootPane().setDefaultButton(loginBtn);
    
    setComponentStyles(panel, "dark");
    screenTitle.setHeading();
    setLocationRelativeTo(null);
    setVisible(true);
  }
  
  /**
   * Create text fields, labels and button for Login form
   */
  private void createLoginForm() {
    userField = new TextField(lgnBox.x, lgnBox.y + 50);
    passField = new JPasswordField();
    loginBtn = new Button(lgnBox.x, lgnBox.y + 180, "Login", secondaryCol);
    registerBtn = new Button(lgnBox.x + 110, lgnBox.y + 180, "Sign Up");
    success = new Label(lgnBox.x, lgnBox.y + 250, "");
    backIconHero = new JLabel(loginHeroImage);
    screenTitle = new Label(lgnBox.x, lgnBox.y - 10, "Login");
    backIconHero.setBounds(200, 250, 400, 400);
    userField.setSize(210, userField.getHeight());
    userField.setCaretColor(Color.WHITE);
    passField.setBounds(lgnBox.x, lgnBox.y + 120, 210, 40);
    placeFieldLabel(userField, "Username", panel);
    placeFieldLabel(passField, "Password", panel);

    panel.add(userField);
    panel.add(passField);
    panel.add(loginBtn);
    panel.add(registerBtn);
    panel.add(success);
    panel.add(backIconHero);
    panel.add(screenTitle);
  }

  /**
   * Set action listener for login button
   */
  public void loginBtnAction() {
    JFrame frame = this;
    loginBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputUser = userField.getText();
        String inputPass = String.valueOf(passField.getPassword());

        if(inputUser.isEmpty() || inputPass.isEmpty()){
          System.out.println("Either input empty..");
          return;
        }

        try {
          if (DatabaseAPI.verifyUser(inputUser, inputPass)) {
            frame.dispose();
            frame.remove(panel);
            User session = DatabaseAPI.getUser(inputUser);
            new HomeUI(session);
          } else {
            success.setText("Wrong username or password");
            passField.setText("");
          }
        } catch (SQLException sqlException) {
          sqlException.printStackTrace();
        }
      }
    });
  }

  /**
   * Set action listener for register button
   */
  public void registerBtnAction() {
    registerBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.removeAll();
        dispose();
        RegisterUI register = new RegisterUI();
        register.setVisible(true);
      }
    });
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
