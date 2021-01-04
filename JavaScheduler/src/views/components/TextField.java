package views.components;

import javax.swing.JTextField;

import views.MasterUI;

public class TextField extends JTextField {

  private static final long serialVersionUID = -2254754514418403224L;

  public TextField(int x, int y, String title) {
    super(title);
    this.setBounds(x, y, 300, 40);
    setDefaultStyle();
  }

  public TextField(String title) {
    super(title);
    setDefaultStyle();
    setSize(300, 40);
  }

  public TextField(int x, int y) {
    super();
    this.setBounds(x, y, 300, 40);
    setDefaultStyle();
  }
  
  /**
   * Set default style for text field
   */
  private void setDefaultStyle() {
    setCaretColor(MasterUI.primaryColAlt);
    setBackground(MasterUI.lightColAlt);
    setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
  }

  /**
   * Set pixel position of text field
   * @param x - Position x value
   * @param y - Positon y value
   */
  public void setPosition(int x, int y) {
    this.setBounds(x, y, this.getWidth(), this.getHeight());
  }

  /**
   * Remove padding and clear background
   */
  public void wipeBackground() {
    setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    setOpaque(false);
  }

  /**
   * Set equal padding on text field
   * @param p - Padding to be set
   */
  public void setEqualPadding(int p) {
    setBorder(javax.swing.BorderFactory.createEmptyBorder(p, p, p, p));
  }

  /**
   * Set placeholder text inside text field
   */
  public void setPlaceholderText() {
    setForeground(MasterUI.lightCol);
  }
}
