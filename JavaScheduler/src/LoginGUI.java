import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginGUI implements ActionListener {

  private static JLabel userLabel;
  private static JTextField userField;
  private static JLabel passLabel;
  private static JPasswordField passField;
  private static JButton button;
  private static JLabel success;

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    frame.setSize(500, 400);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(panel);
    panel.setLayout(null);

    userLabel = new JLabel("User", SwingConstants.CENTER);
    userLabel.setBounds(10, 20, 80, 25);
    panel.add(userLabel);

    passLabel = new JLabel("Password");
    passLabel.setBounds(10, 50, 80, 25);
    panel.add(passLabel);

    userField = new JTextField();
    userField.setBounds(100, 20, 165, 25);
    panel.add(userField);

    passField = new JPasswordField();
    passField.setBounds(100, 50, 165, 25);
    panel.add(passField);

    button = new JButton("Login");
    button.setBounds(100, 80, 80, 25);
    button.addActionListener(new LoginGUI());
    panel.add(button);

    success = new JLabel("");
    success.setBounds(100, 110, 300, 25);
    panel.add(success);

    frame.setVisible(true);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    String user = userField.getText();
    String password = String.valueOf(passField.getPassword());
    System.out.println(user + ", " + password);

    if(user.equals("User") && password.equals("123")){
      success.setText("Success");
    } else {
      success.setText("Wrong Credentials");
    }
  }
}
