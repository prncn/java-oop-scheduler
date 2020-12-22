package views.components;

import views.MasterUI;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Panel extends JPanel {
  public Panel(JFrame frame) {
    this.setBounds(200, 0, frame.getWidth() - 200, frame.getHeight());
    this.setBackground(MasterUI.getColor("primaryCol"));
  }
}
