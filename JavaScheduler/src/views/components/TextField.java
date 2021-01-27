package views.components;

import views.MasterUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.List;

public class TextField extends JTextField {

  private static final long serialVersionUID = -2254754514418403224L;
  private Label errorLabel;

  /**
   * Nested class to limit the number of entered characters in a textfield
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
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
        throws BadLocationException {
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

  /**
   * Constructor for positioning and text
   * 
   * @param x
   * @param y
   * @param title
   */
  public TextField(int x, int y, String title) {
    super(title);
    this.setBounds(x, y, 300, 40);
    setDefaultStyle();
  }

  /**
   * Constructor for text only, position has to be set seperately
   * 
   * @param title
   */
  public TextField(String title) {
    super(title);
    setDefaultStyle();
    setSize(300, 40);
  }

  /**
   * Constructor for default text field, only position
   * 
   * @param x
   * @param y
   */
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
   * Remove padding and clear background
   */
  public void wipeBackground() {
    setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    setOpaque(false);
  }

  /**
   * Set equal padding on text field
   * 
   * @param p - Padding to be set
   */
  public void setEqualPadding(int p) {
    setBorder(javax.swing.BorderFactory.createEmptyBorder(p, p, p, p));
  }

  public Button appendButton(ImageIcon icon) {
    Button btn = new Button(getX() + getWidth(), getY(), "", getBackground());
    btn.setSize(getHeight(), getHeight());
    btn.setIcon(icon);
    return btn;
  }

  /**
   * Get error label that was created to the field
   * 
   * @return
   */
  public Label getErrorLabel() {
    return errorLabel;
  }

  /**
   * Create error label that appears on specific (error) behaviour on a text field
   * 
   * @param msg
   * @return Label object for error messaging
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

  public void setSuggestionField() {
    getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        updateSuggest();
      }

      public void removeUpdate(DocumentEvent e) {
        updateSuggest();
      }

      public void insertUpdate(DocumentEvent e) {
        updateSuggest();
      }

      public void updateSuggest() {
        System.out.println("change detected");
      }
    });
  }

  /**
   * Overload method to set default scroll option length to <code>2</code>
   * @see #setDropdown(List, JScrollPane, Panel, ActionListener, int)
    * @param <T> - Generic entry type, i.e. <code>Event.location</code>
   * @param entries - List of entries that should be displayed on the menu
   * @param scroll - Scroll pane that should contain the menu panel
   * @param panel - Panel on which the dropdown menu is placed
   * @param action - ActionListener that specifies the action on clicking a menu option
   * @return An array that returns the scroll panel on the first index and the inner
   *         panel on the second index
   */
  public <T> Component[] setDropdown(List<T> entries, JScrollPane scroll, Panel panel, ActionListener action) {
    return setDropdown(entries, scroll, panel, action, 2);
  }

  /**
   * Append a dropdown menu onto the text field
   * @param <T> - Generic entry type, i.e. <code>Event.location</code>
   * @param entries - List of entries that should be displayed on the menu
   * @param scroll - Scroll pane that should contain the menu panel
   * @param panel - Panel on which the dropdown menu is placed
   * @param action - ActionListener that specifies the action on clicking a menu option
   * @return An array that returns the scroll panel on the first index and the inner
   *         panel on the second index
   */
  public <T> Component[] setDropdown(List<T> entries, JScrollPane scroll, Panel panel, ActionListener action, int options) {
    options = Math.min(options, entries.size());
    if (scroll != null) {
      panel.remove(scroll);
      panel.repaint();
      scroll = null;
      return new Component[]{scroll, panel};
    }

    Panel dppanel = new Panel();
    int HGHT = 38;
    dppanel.setBounds(0, 0, getWidth() + 30, HGHT * entries.size());
    dppanel.setPreferredSize(new Dimension(getWidth(), HGHT * entries.size()));
    int y = 0;
    for (T entry : entries) {
      Button<T> lcBtn = new Button<>(0, y, entry.toString(), MasterUI.lightColAlt);
      lcBtn.bindData(entry);
      lcBtn.setColor(MasterUI.lightColAlt);
      lcBtn.setSize(dppanel.getWidth(), HGHT);
      lcBtn.setDark(false);
      lcBtn.setHorizontalAlignment(SwingConstants.LEFT);
      lcBtn.addActionListener(action);
      lcBtn.addActionListener(e -> { setText(entry.toString()); panel.repaint(); });
      dppanel.add(lcBtn);
      y += lcBtn.getHeight();
    }
    scroll = new JScrollPane(dppanel);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    if (options == entries.size()) scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(14);
    scroll.setBorder(BorderFactory.createEmptyBorder());
    scroll.setBounds(getX(), getY() + getHeight(), getWidth() + 40, HGHT * options);
    MasterUI.setComponentStyles(dppanel, "light");

    panel.add(this);
    panel.add(scroll);
    panel.setComponentZOrder(scroll, 0);
    panel.revalidate();
    panel.repaint();

    return new Component[]{ scroll, panel };
  }

  /**
   * Set max. length of a entered characters in a textfield
   */
  public void setMaximumLength(int limit) {
    ((AbstractDocument) this.getDocument()).setDocumentFilter(new LimitDocumentFilter(limit));
  }
}
