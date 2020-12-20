import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Point;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class LoginGUI extends MasterAbstractGUI {
  private static final long serialVersionUID = -6864342302747015773L;

  /**
   * UI components
   */
  private static JLabel userLabel;
  protected static JTextField userField;
  private static JLabel passLabel;
  protected static JPasswordField passField;
  protected static JButton loginBtn;
  protected static JButton registerBtn;
  protected static JLabel success;
  protected JLabel backIconHero;
  protected JLabel screenTitle;
  protected static Point lgnBox = new Point(100, 70);

  // protected static JPanel panel = new JPanel();

  public LoginGUI() {
    this.setTitle("Scheduler Login");
    this.setSize(600, 500);

    userLabel = createLabel(lgnBox.x, lgnBox.y + 30, "Username");
    passLabel = createLabel(lgnBox.x, lgnBox.y + 100, "Password");
    userField = new JTextField();
    passField = new JPasswordField();
    loginBtn = createButton(lgnBox.x, lgnBox.y + 180, "Login", accentCol);
    registerBtn = createButton(lgnBox.x + 110, lgnBox.y + 180, "Sign Up", primaryCol);
    success = createLabel(lgnBox.x, lgnBox.y + 250, "");
    backIconHero = new JLabel(loginHeroImage);
    screenTitle = new JLabel("Login to Your Account");

    backIconHero.setBounds(200, 250, 400, 400);
    screenTitle.setBounds(lgnBox.x, lgnBox.y, 500, 25);

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
            HomeGUI home = new HomeGUI();
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
        RegisterGUI register = new RegisterGUI();
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
    screenTitle.setFont(bodyFont.deriveFont(Font.BOLD, 20f));
    this.setLocationRelativeTo(null);
  }

  public static void main(String[] args) {
    LoginGUI logFrame = new LoginGUI();
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
