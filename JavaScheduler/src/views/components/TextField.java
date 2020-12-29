package views.components;

import javax.swing.JTextField;

public class TextField extends JTextField {
  
  private static final long serialVersionUID = -2254754514418403224L;

  public TextField(int x, int y, String title) {
    super(title);
    this.setBounds(x, y, 300, 40);
  }

  public TextField(int x, int y) {
    super();
    this.setBounds(x, y, 300, 40);
  }

  public void setPosition(int x, int y) {
    this.setBounds(x, y, this.getWidth(), this.getHeight());
  }
}
