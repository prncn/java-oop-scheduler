package views.components;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import views.MasterUI;

public class Label extends JLabel {

  private static final long serialVersionUID = 1L;

  public Label(int x, int y, String text) {
    super(text);
    this.setFont(MasterUI.bodyFont);
    this.setBounds(x, y, 250, 25);
  }

  public Label(String text) {
    super(text);
    this.setSize(250, 25);
  }

  public Label() {
    super();
  }

  public Label(ImageIcon image) {
    super(image);
  }

  public void setHeading() {
    this.setSize(500, 40);
    this.setFont(this.getFont().deriveFont(Font.BOLD, 30f));
  }

  public void setPosition(int x, int y) {
    this.setBounds(x, y, this.getWidth(), this.getHeight());
  }
}
