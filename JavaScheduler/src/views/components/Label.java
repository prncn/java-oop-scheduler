package views.components;

import java.awt.Font;
import javax.swing.JLabel;

public class Label extends JLabel {

  private static final long serialVersionUID = 1L;

  public Label(int x, int y, String text) {
    super(text);
    this.setBounds(x, y, 250, 25);
  }

  public void setTitle() {
    this.setSize(500, 30);
    this.setFont(this.getFont().deriveFont(Font.BOLD, 20f));
  }

}
