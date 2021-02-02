package views.components;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Component;

import views.MasterUI;

public class Label extends JLabel {

  private static final long serialVersionUID = 1L;
  private boolean isHeading = false;
  private boolean unset = false;

  public Label(int x, int y, String text) {
    super(text);
    setFont(MasterUI.robotoFont);
    setBounds(x, y, 250, 25);
  }

  public Label() {
    super();
    setFont(MasterUI.robotoFont);
    setSize(250, 25);
  }

  public Label(String text, Component cmp) {
    super(text);
    setFont(MasterUI.robotoFont);
    setBounds(cmp.getX() + cmp.getWidth() + 12, cmp.getY(), 250, cmp.getHeight());
  }

  public Label(ImageIcon image) {
    super(image);
  }

  public void setHeading() {
    isHeading = true;
    setUnset(true);
    setSize(500, 40);
    setFont(getFont().deriveFont(Font.BOLD, 30f));
  }

  
  public boolean getHeading() {
    return isHeading;
  }

  public void setUnset(boolean unset) {
    this.unset = unset;
  }

  public boolean getUnset() {
    return unset;
  }

  public void setPosition(int x, int y) {
    setBounds(x, y, getWidth(), getHeight());
  }

  public void appendIcon(ImageIcon icon) {
    setIcon(icon);
    setVerticalTextPosition(SwingConstants.BOTTOM);
    setIconTextGap(10);
  }

  public void fillIcon(ImageIcon icon) {
    setIcon(icon);
    setSize(getIcon().getIconWidth(), getIcon().getIconHeight());
  }
}
