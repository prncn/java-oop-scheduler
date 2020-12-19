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
  private static JTextField userField;
  private static JLabel passLabel;
  private static JPasswordField passField;
  private JButton loginBtn;
  private JButton registerBtn;
  private JLabel success;
  private JLabel backIconHero;
  private JLabel screenTitle;

  // protected static JPanel panel = new JPanel();

  public LoginGUI() {
    this.setTitle("Scheduler Login");
    this.setSize(600, 500);
    Point lgnBox = new Point(100, 70);

    userLabel = new JLabel("Username");
    passLabel = new JLabel("Password");
    userField = new JTextField();
    passField = new JPasswordField();
    loginBtn = new JButton("Login");
    registerBtn = new JButton("Sign Up");
    success = new JLabel();
    backIconHero = new JLabel(heroImage);
    screenTitle = new JLabel("Log In");

    backIconHero.setBounds(200, 150, 400, 400);
    screenTitle.setBounds(lgnBox.x, lgnBox.y, 100, 25);

    userLabel.setBounds(lgnBox.x, lgnBox.y + 30, 100, 25);
    userField.setBounds(lgnBox.x, lgnBox.y + 50, 210, 40);

    passLabel.setBounds(lgnBox.x, lgnBox.y + 100, 100, 25);
    passField.setBounds(lgnBox.x, lgnBox.y + 120, 210, 40);

    loginBtn.setBounds(lgnBox.x, lgnBox.y + 180, 100, 40);
    loginBtn.setBackground(accentCol);

    loginBtn.addMouseListener(new MouseAdapter() {
      public void mouseEntered(MouseEvent e) {
        loginBtn.setBackground(accentColDark);
      }

      public void mouseExited(MouseEvent e) {
        loginBtn.setBackground(accentCol);
      }
    });
    this.getRootPane().setDefaultButton(loginBtn);

    loginBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputUser = userField.getText();
        String inputPass = String.valueOf(passField.getPassword());

        try {
          if (DataBaseAPI.verifyUser(inputUser, inputPass)) {
            dispose();
            userWelcome.setText("Hi, " + inputUser + "!");
            HomeGUI home = new HomeGUI();
            home.setVisible(true);
          } else {
            success.setText("Wrong Credentials");
            passField.setText("");
          }
        } catch (SQLException sqlException) {
          sqlException.printStackTrace();
        }
      }
    });

    registerBtn.setBounds(lgnBox.x + 110, lgnBox.y + 180, 100, 40);
    registerBtn.setBackground(primaryCol);
    success.setBounds(lgnBox.x, lgnBox.y + 250, 300, 25);

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
  }

  public static void main(String[] args) {
    LoginGUI logFrame = new LoginGUI();
    logFrame.setVisible(true);
    logFrame.setLocationRelativeTo(null);
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
