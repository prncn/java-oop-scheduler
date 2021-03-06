package views.components;

import controllers.DatabaseAPI;
import models.User;
import views.MasterUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
  /** Generate TextFields that can be used for other Components*/
public class TextField extends JTextField {

  /** Generated serial ID */
  private static final long serialVersionUID = -2254754514418403224L;

  /** Error label hint to be placed on textfield */
  private Label errorLabel;

  /** Helper attribute for components to build suggestions field  */
  private Component[] comps;

  /** Default width of textfield */
  private final int WIDTH = 310;

  /** Default height of textfield */
  private final int HEIGHT = 40;

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
   * @param x - x coordinate for bound
   * @param y - y coordinate for bound
   * @param title - text on the textfield 
   */
  public TextField(int x, int y, String title) {
    super(title);
    this.setBounds(x, y, WIDTH, HEIGHT);
    setDefaultStyle();
  }

  /**
   * Constructor for text only, position has to be set seperately
   * 
   * @param title - text on the textfield.
   */
  public TextField(String title) {
    super(title);
    setDefaultStyle();
    setSize(WIDTH, HEIGHT);
  }

  /**
   * Constructor for default text field, only position
   * 
   * @param x - x coordinate for bound
   * @param y - y coordinate for bound
   */
  public TextField(int x, int y) {
    super();
    this.setBounds(x, y, WIDTH, HEIGHT);
    setDefaultStyle();
  }

  /**
   * Set default style for text field
   */
  public void setDefaultStyle() {
    setCaretColor(MasterUI.primaryColAlt);
    setBackground(MasterUI.lightColAlt);
    setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
  }

  /**
   * Remove padding and clear background
   */
  public void wipeBackground() {
    setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
    setBackground(MasterUI.lightCol);
    setOpaque(true);
  }

  /**
   * Set equal padding on text field
   * 
   * @param p - Padding to be set
   */
  public void setEqualPadding(int p) {
    setBorder(javax.swing.BorderFactory.createEmptyBorder(p, p, p, p));
  }

  /**
   * Append button to field, on the right.
   * 
   * @param icon - Icon to be set into the button
   * @return Button on text field
   */
  public Button appendButton(ImageIcon icon) {
    setSize(getWidth() - getHeight(), getHeight());
    Button btn = new Button(getX() + getWidth() - 20, getY(), "", getBackground());
    btn.setSize(getHeight() + 20, getHeight());
    btn.setCornerRadius(Button.SMOOTH);
    btn.setIcon(icon);
    return btn;
  }

  /**
   * Get error label that was created to the field
   * 
   * @return the error label created to the field
   */
  public Label getErrorLabel() {
    return errorLabel;
  }

  /**
   * Create error label that appears on specific (error) behaviour on a text field
   * 
   * @param msg - message shown on error label
   * @return Label object for error messaging
   */
  public Label createErrorLabel(String msg) {
    int xPos = (getX() + getWidth()) - 250;
    Label error;
    switch(msg) {
      case "Date" : {
        error = new Label(xPos + 55, getY() + getHeight(), "");
        break;
      }
      case "Location" : {
         error = new Label(xPos + 40, getY() + getHeight(), "");
        break;
      }
      default: {
        error = new Label(xPos, getY() + getHeight(), "");
        break;
      }
    }
    error.setForeground(Color.RED);
    error.setHorizontalAlignment(SwingConstants.RIGHT);
    error.setUnset(true);
    error.setName(msg);
    errorLabel = error;
    return error;
  }

  /**
   * Create user search suggestion below text field. On each key press, new
   * suggestions are loaded in through the <code>setDropdown</code> method.
   * 
   * @param <T>     - Generic entry type, i.e. <code>Event.location</code>
   * @param scroll  - Scroll pane that should contain the menu panel
   * @param panel   - Panel on which the dropdown menu is placed
   * @param action  - ActionListener that specifies the action on clicking a menu option
   * @return the user suggestion
   */
  public <T> Component[] setSuggestionField(JScrollPane scroll, Panel panel, ActionListener action) {
    comps = new Component[2];
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
        if (!getText().isBlank()) {
          List<User> entries = DatabaseAPI.getAllUsers();
          entries.removeIf(e -> !e.getUsername().startsWith(getText()));
          Component[] _comps = setDropdown(entries, scroll, panel, action, entries.size());
          comps[0] = (JScrollPane) _comps[0];
          comps[1] = (Panel) _comps[1];
          System.out.println("Here: " + comps[0]);
        }
      }
    });

    return comps;
  }

  /**
   * Overload method to set default scroll option length to <code>2</code>
   * 
   * @see #setDropdown(List, JScrollPane, Panel, ActionListener, int)
   * @param <T>     - Generic entry type, i.e. <code>Event.location</code>
   * @param entries - List of entries that should be displayed on the menu
   * @param scroll  - Scroll pane that should contain the menu panel
   * @param panel   - Panel on which the dropdown menu is placed
   * @param action  - ActionListener that specifies the action on clicking a menu
   *                option
   * @return An array that returns the scroll panel on the first index and the
   *         inner panel on the second index
   */
  public <T> Component[] setDropdown(List<T> entries, JScrollPane scroll, Panel panel, ActionListener action) {
    return setDropdown(entries, scroll, panel, action, 2);
  }

  /**
   * Append a dropdown menu onto the text field
   * 
   * @param <T>     - Generic entry type, i.e. <code>Event.location</code>
   * @param entries - List of entries that should be displayed on the menu
   * @param scroll  - Scroll pane that should contain the menu panel
   * @param panel   - Panel on which the dropdown menu is placed
   * @param action  - ActionListener that specifies the action on clicking a menu option
   * @param options - number of options for menu
   * @return An array that returns the scroll panel on the first index and the
   *         inner panel on the second index
   */
  public <T> Component[] setDropdown(List<T> entries, JScrollPane scroll, Panel panel, ActionListener action,
      int options) {
    options = Math.min(options, entries.size());
    if (scroll != null) {
      panel.remove(scroll);
      panel.repaint();
      scroll = null;
      return new Component[] { scroll, panel };
    }

    Panel dppanel = new Panel();
    int HGHT = 38;
    dppanel.setBounds(0, 0, getWidth() + 40, HGHT * entries.size());
    dppanel.setPreferredSize(new Dimension(getWidth(), HGHT * entries.size()));
    int y = 0;
    for (T entry : entries) {
      DataButton<T> lcBtn = new DataButton<>(0, y, entry.toString(), MasterUI.lightColAlt);
      lcBtn.bind(entry);
      lcBtn.setColor(MasterUI.lightColAlt);
      lcBtn.setSize(getWidth() + 40, HGHT);
      lcBtn.setDark(false);
      lcBtn.setHorizontalAlignment(SwingConstants.LEFT);
      lcBtn.addActionListener(action);
      lcBtn.addActionListener(e -> {
        setText(entry.toString());
        panel.repaint();
      });
      dppanel.add(lcBtn);
      y += lcBtn.getHeight();
    }
    scroll = new JScrollPane(dppanel);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    if (options == entries.size())
      scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scroll.getVerticalScrollBar().setUnitIncrement(14);
    scroll.setBorder(BorderFactory.createEmptyBorder());
    scroll.setBounds(getX(), getY() + getHeight() - 6, getWidth() - 18, HGHT * options);
    MasterUI.setComponentStyles(dppanel, "light");

    panel.add(this);
    panel.add(scroll);
    panel.setComponentZOrder(scroll, 0);
    panel.revalidate();
    panel.repaint();

    return new Component[] { scroll, panel };
  }

  /**
   * Set max. length of a entered characters in a textfield
   * @param limit - defines the maximum length
   */
  public void setMaximumLength(int limit) {
    ((AbstractDocument) this.getDocument()).setDocumentFilter(new LimitDocumentFilter(limit));
  }

  @Override
  protected void paintComponent(Graphics g) {
    setOpaque(false);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(getBackground());
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    super.paintComponent(g);
  }
}
