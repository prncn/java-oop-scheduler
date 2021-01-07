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
  private boolean isTab;
  private ActionListener switchPanelAction;
  private Color prevColor;

  public Button(int x, int y, String text, Color color) {
    super(text);
    drawDefaultStyle();
    this.color = color;
    setPosition(x, y);
    setBackground(color);
    isTab = false;
    dark = true;
    filled = true;
  }

  public Button(int x, int y, String text) {
    super(text);
    drawDefaultStyle();
    this.setPosition(x, y);
    this.setBackground(MasterUI.primaryCol);
    isTab = false;
    dark = true;
    filled = false;
  }

  public Button(int x, int y, String text, JPanel switchTo) {
    super(text);
    drawDefaultStyle();
    switchPanelAction = HomeUI.switchPanelAction(switchTo);
    addActionListener(switchPanelAction);
    setPosition(x, y);
    setBackground(MasterUI.primaryColAlt);
    setTab();
    color = MasterUI.primaryColAlt;
    isTab = true;
    dark = true;
    filled = true;
  }

  /**
   * Change reference of Panel to which the button should link to
   * @param switchTo - Panel object to be set
   */
  public void changeReferencePanel(JPanel switchTo) {
    removeActionListener(switchPanelAction);
    switchPanelAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HomeUI.switchPanel(switchTo);
      }
    };
    addActionListener(switchPanelAction);
  }

  /**
   * Set default button styles
   */
  public void drawDefaultStyle() {
    setFont(MasterUI.robotoFont);
    setCursor(cursor);
    setSize(width, height);
    setBorderPainted(false);
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
   * Get wether button is set on dark mode
   * 
   * @return Boolean object instead of boolean primitive since unset buttons may
   *         return null
   */
  public Boolean getDark() {
    return this.dark;
  }

  /**
   * Set dark mode value
   * 
   * @param value - Boolean <code>true</code> if dark mode should be enabled
   */
  public void setDark(boolean value) {
    if(getDark() == value) return;
    this.dark = value;
    if (value)
      this.setForeground(Color.WHITE);
    else
      this.setForeground(Color.BLACK);
  }


  /**
   * Get previously set color
   * @return Color
   */
  public Color getPrevColor() {
    return this.prevColor;
  }

  /**
   * Store current color, to be accessed later
   * @param color - Current color
   */
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

  /**
   * Transform button to tab button. Tabs are wider
   * and uncolored. Mainly used for panel navigation.
   */
  public void setTab() {
    setSize(200, 50);
    setHorizontalAlignment(SwingConstants.LEFT);
    setMargin(new Insets(5, 5, 10, 10));
    setFont(this.getFont().deriveFont(13f));
    setForeground(Color.WHITE);
    setFocusPainted(false);
    setContentAreaFilled(true);
    setIconTextGap(15);
    isTab = true;
  }

  /**
   * Transform button to small button. Small button are
   * uncolored.
   */
  public void setSmall() {
    setMargin(new Insets(0, 0, 0, 0));
    setDark(false);
    setColor(MasterUI.lightCol);
    setSize(70, 40);
  }

  /**
   * Get wether button is a tab button
   * 
   * @return boolean is tab or not
   */
  public boolean getTab() {
    return this.isTab;
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

  @Override
  public void mousePressed(MouseEvent e) {
    this.setBackground(color);
  }

  public void mouseReleased(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}

}