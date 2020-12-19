import java.awt.Font;

import javax.swing.JLabel;

public class HomeGUI extends MasterAbstractGUI {
  private static final long serialVersionUID = -771654490802003766L;

  public HomeGUI() {
    panel.removeAll();
    this.setTitle("Meetings Scheduler");
    this.setSize(1200, 700);

    if(userWelcome.getText().equals("")){
      userWelcome.setText("Hello Guest!");
    }
    userWelcome.setBounds(100, 90, 200, 50);
    panel.add(userWelcome);

    setComponentStyles();
    userWelcome.setFont(bodyFont.deriveFont(Font.BOLD, 30f));
    this.setLocationRelativeTo(null);
  }
  
  public static void main(String[] args) {
    HomeGUI homeFrame = new HomeGUI();
    homeFrame.setVisible(true);
  }
  
}
