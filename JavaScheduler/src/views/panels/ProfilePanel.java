package views.panels;

import java.awt.Color;
import javax.swing.JFrame;
import views.components.Panel;

public class ProfilePanel extends Panel {

  private static final long serialVersionUID = 1L;

  public ProfilePanel(JFrame frame) {
    super(frame);
    this.setBackground(Color.RED);
  }
}