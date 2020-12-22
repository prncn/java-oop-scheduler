package views.components;

import views.MasterUI;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Panel extends JPanel {

  private static final long serialVersionUID = -3799733211815510931L;

  public Panel() {
    super();
  }

  public Panel(JFrame frame) {
    this.setBounds(200, 0, frame.getWidth() - 200, frame.getHeight());
    this.setBackground(MasterUI.getColor("lightCol"));
  }

  public void updateBounds(JFrame frame) {
    this.setBounds(0, 0, frame.getWidth(), frame.getHeight());
  }
}