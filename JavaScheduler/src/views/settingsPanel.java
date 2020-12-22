package views;

import java.awt.Color;
import javax.swing.JFrame;
import views.components.Panel;

public class settingsPanel extends Panel {

  private static final long serialVersionUID = 1L;

  public settingsPanel(JFrame frame) {
    super(frame);
    this.setBackground(Color.RED);
  }
}
