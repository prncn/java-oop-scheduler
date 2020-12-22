package views.components;

import javax.swing.JTextField;

public class TextField extends JTextField {
  public TextField(int x, int y, String title) {
    super(title);
    this.setBounds(x, y, 300, 40);
  }
  public TextField(int x, int y) {
    super();
    this.setBounds(x, y, 300, 40);
  }
}
