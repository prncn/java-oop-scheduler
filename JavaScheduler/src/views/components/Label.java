package views.components;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import views.MasterUI;

public class Label extends JLabel {

  private static final long serialVersionUID = 1L;
  private boolean isHeading = false;

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
    isHeading = true;
    this.setSize(500, 40);
    this.setFont(this.getFont().deriveFont(Font.BOLD, 30f));
  }

  public boolean getHeading() {
    return this.isHeading;
  }

  public void setPosition(int x, int y) {
    this.setBounds(x, y, this.getWidth(), this.getHeight());
  }

  public void appendIcon(ImageIcon icon) {
    this.setIcon(icon);
    this.setVerticalTextPosition(SwingConstants.BOTTOM);
    this.setIconTextGap(10);
  }
}
