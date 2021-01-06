package views.components;

import views.MasterUI;
import views.HomeUI;

import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Button extends JButton implements MouseListener {

  private static final long serialVersionUID = 1L;
  private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
  private Color color;
  private int width = 100;
  private int height = 40;
  private boolean dark;
  private boolean filled;
  private boolean tabbed;
  private ActionListener switchPanelAction;
  private Color prevColor;

  public Button(int x, int y, String text, Color color) {
    super(text);
    drawDefaultStyle();
    this.color = color;
    this.setPosition(x, y);
    this.setContentAreaFilled(false);
    this.setBackground(color);
    tabbed = false;
    dark = true;
    filled = true;
  }

  public Button(int x, int y, String text) {
    super(text);
    drawDefaultStyle();
    this.setPosition(x, y);
    this.setBackground(MasterUI.primaryCol);
    tabbed = false;
    dark = true;
    filled = false;
  }

  public Button(int x, int y, String text, JPanel switchTo) {
    super(text);
    drawDefaultStyle();
    this.color = MasterUI.primaryColAlt;
    this.setPosition(x, y);
    this.setBackground(MasterUI.primaryColAlt);
    this.setSize(200, 50);
    this.setHorizontalAlignment(SwingConstants.LEFT);
    this.setMargin(new Insets(10, 10, 10, 10));
    this.setFont(this.getFont().deriveFont(13f));
    this.setForeground(Color.WHITE);
    this.setFocusPainted(false);
    this.setIconTextGap(15);
    tabbed = true;
    dark = true;
    filled = true;

    switchPanelAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HomeUI.switchPanel(switchTo);
      }
    };

    this.addActionListener(switchPanelAction);
  }

  /**
   * Change reference of Panel to which the button should link to
   * @param switchTo - Panel object to be set
   */
  public void changeReferencePanel(JPanel switchTo) {
    this.removeActionListener(switchPanelAction);

    switchPanelAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HomeUI.switchPanel(switchTo);
      }
    };

    this.addActionListener(switchPanelAction);
  }

  /**
   * Set default button styles
   */
  public void drawDefaultStyle() {
    this.setFont(MasterUI.robotoFont);
    this.setCursor(cursor);
    this.setSize(width, height);
    this.setBorderPainted(false);
    addMouseListener(this);
  }

  /**
   * Set position of button
   * 
   * @param x - Horizontal position coordinate
   * @param y - Vertical position coordinate
   */
  public void setPosition(int x, int y) {
    this.setBounds(x, y, this.getWidth(), this.getHeight());
  }

  /**
   * Set dark mode value
   * 
   * @param bool - Boolean enable/disable dark mode
   */
  public void setDark(boolean bool) {
    this.dark = bool;
    if (bool)
      this.setForeground(Color.WHITE);
    else
      this.setForeground(Color.BLACK);
  }

  /**
   * Get wether button is set on dark mode
   * 
   * @return Boolean object instead of boolean primitive since unset buttons may
   *         return null
   */
  public Boolean getDark() {
    return this.dark;
  }

  public Color getPrevColor() {
    return this.prevColor;
  }

  public void setPrevColor(Color color) {
    this.prevColor = color;
  }

  /**
   * Set color of button
   * 
   * @param color - Color to be set
   */
  public void setColor(Color color) {
    this.setBackground(color);
    this.color = color;
  }

  public Color getColor() {
    return this.color;
  }

  public void setTab() {
    this.setSize(200, 50);
    this.setHorizontalAlignment(SwingConstants.LEFT);
    this.setMargin(new Insets(5, 5, 10, 10));
    this.setFont(this.getFont().deriveFont(13f));
    this.setForeground(Color.WHITE);
    this.setFocusPainted(false);
    this.setContentAreaFilled(true);
    this.setIconTextGap(15);
    tabbed = true;
  }

  /**
   * Get wether button is a tab button
   * 
   * @return boolean is tab or not
   */
  public boolean getTab() {
    return this.tabbed;
  }

  /**
   * Center text inside button
   */
  public void centerText() {
    setVerticalTextPosition(SwingConstants.BOTTOM);
    setHorizontalAlignment(SwingConstants.CENTER);
    setVerticalAlignment(SwingConstants.CENTER);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    if (this.filled) {
      this.setBackground(color.darker());
    }
  }

  @Override
  public void mouseExited(MouseEvent e) {
    if (this.filled) {
      this.setBackground(color);
    }
  }

  public void mousePressed(MouseEvent e) {
    this.setBackground(color);
  }

  public void mouseReleased(MouseEvent e) {
  }

  public void mouseClicked(MouseEvent e) {
  }

}
