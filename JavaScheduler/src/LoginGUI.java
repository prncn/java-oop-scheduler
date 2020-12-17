import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Insets;
import java.awt.Component;
import java.awt.Point;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class LoginGUI extends JFrame {

  /**
   * UI Components
   */
  private static JLabel userLabel;
  private static JTextField userField;
  private static JLabel passLabel;
  private static JPasswordField passField;
  private static JButton loginBtn;
  private static JButton registerBtn;
  private static JLabel success;

  /**
   * Colors and Fonts
   */
  private static Color primaryCol = new Color(50, 50, 65);
  private static Color primaryColAlt = new Color(60, 60, 75);
  private static Color accentCol = new Color(116, 207, 183);
  private static Font bodyFont;

  public LoginGUI() {
    JPanel panel = new JPanel();
    this.setTitle("Scheduler Login");
    ImageIcon favicon = new ImageIcon("favicon-96x96.png");
    this.setIconImage(favicon.getImage());
    this.setSize(600, 500);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(panel);

    userLabel = new JLabel("Username");
    passLabel = new JLabel("Password");
    userField = new JTextField();
    passField = new JPasswordField();
    loginBtn = new JButton("Login");
    registerBtn = new JButton("Sign Up");
    success = new JLabel();

    userLabel.setBounds(100, 40, 100, 25);
    userField.setBounds(100, 60, 210, 40);
    userField.setBackground(primaryColAlt);
    userField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

    passLabel.setBounds(100, 120, 100, 25);
    passField.setBounds(100, 140, 210, 40);
    passField.setBackground(primaryColAlt);
    passField.setBorder(javax.swing.BorderFactory.createEmptyBorder());

    loginBtn.setBounds(100, 210, 100, 40);
    loginBtn.setFocusPainted(false);
    loginBtn.setContentAreaFilled(true);
    loginBtn.setBackground(accentCol);
    loginBtn.setMargin(new Insets(5, 5, 3, 3));
    this.getRootPane().setDefaultButton(loginBtn);
    
    loginBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        String inputUser = userField.getText();
        String inputPass = String.valueOf(passField.getPassword());

        if(inputUser.equals("User123") && inputPass.equals("123")){
          success.setText("Proceeding...");
        } else {
          success.setText("Wrong Credentials");
          passField.setText("");
        }
      }
    });

    registerBtn.setBounds(210, 210, 100, 40);
    registerBtn.setFocusPainted(false);
    registerBtn.setContentAreaFilled(true);
    registerBtn.setBackground(primaryCol);
    registerBtn.setMargin(new Insets(5, 5, 3, 3));
    success.setBounds(100, 280, 300, 25);

    panel.setLayout(null);
    panel.add(userLabel);
    panel.add(passLabel);
    panel.add(userField);
    panel.add(passField);
    panel.add(loginBtn);
    panel.add(registerBtn);
    panel.add(success);
    panel.setBackground(primaryCol);

    Font bodyFontAlt = new Font("Arial", Font.BOLD, 15);
    Font monoFont = new Font("Consolas", Font.PLAIN, 18);
    File fileRoot = new File(System.getProperty("user.dir"));

    try {
      bodyFont = Font
        .createFont(Font.TRUETYPE_FONT, new File(fileRoot + "/JavaScheduler/assets/fonts/UniversLTStd.otf"))
        .deriveFont(15f);
    } catch (IOException | FontFormatException e) {
      System.out.println(e);
      bodyFont = bodyFontAlt;
    }

    for (Component c : panel.getComponents()) {
      if (c instanceof JLabel) {
        c.setFont(bodyFont);
        c.setForeground(Color.WHITE);
      }
      if (c instanceof JTextField || c instanceof JButton) {
        c.setFont(monoFont);
        c.setForeground(Color.WHITE);
      }
      passField.setCaretColor(Color.WHITE);
      userField.setCaretColor(Color.WHITE);
    }

    UIDefaults uiDefaults = UIManager.getDefaults();
    uiDefaults.put("activeCaption", new javax.swing.plaf.ColorUIResource(Color.gray));
    uiDefaults.put("activeCaptionText", new javax.swing.plaf.ColorUIResource(Color.white));
    JFrame.setDefaultLookAndFeelDecorated(true);
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
