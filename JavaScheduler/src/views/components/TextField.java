package views.components;

import views.MasterUI;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.awt.Color;

public class TextField extends JTextField {

  private static final long serialVersionUID = -2254754514418403224L;
  private Label errorLabel;

  /**
   * Nested class to limit the number of entered characters
   * in a textfield
   */
  public class LimitDocumentFilter extends DocumentFilter {
    private int limit;
    public LimitDocumentFilter(int limit) {
      if (limit <= 0) {
        throw new IllegalArgumentException("Limit can not be <= 0");
      }
      this.limit = limit;
    }
    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
      int currentLength = fb.getDocument().getLength();
      int overLimit = (currentLength + text.length()) - limit - length;
      if (overLimit > 0) {
        text = text.substring(0, text.length() - overLimit);
      }
      if (text.length() > 0) {
        super.replace(fb, offset, length, text, attrs);
      }
    }
  }

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

  public Button appendButton(ImageIcon icon) {
    Button btn = new Button(getX() + getWidth(), getY(), "", MasterUI.primaryColAlt);
    btn.setSize(getHeight(), getHeight());
    btn.setIcon(icon);
    return btn;
  }

  /**
   * Get error label that was created to the field
   * @return
   */
  public Label getErrorLabel() {
    return errorLabel;
  }

  /**
   * Create error label that appears on specific (error) behaviour on 
   * a text field
   * @param msg
   * @return
   */
  public Label createErrorLabel(String msg) {
    int xPos = (getX() + getWidth()) - 250;
    Label error = new Label(xPos, getY() + getHeight(), "");
    error.setForeground(Color.RED);
    error.setHorizontalAlignment(SwingConstants.RIGHT);
    error.setUnset(true);
    error.setName(msg);
    errorLabel = error;
    return error;
  }

  /**
   * Set max. length of a entered characters in a textfield
   */
  public void setMaximumLength(int limit) {
    ((AbstractDocument)this.getDocument()).setDocumentFilter(new LimitDocumentFilter(limit));
  }
}
