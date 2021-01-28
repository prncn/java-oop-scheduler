package views.components;

import views.MasterUI;
import views.HomeUI;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
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
  private boolean dark = true;
  private boolean filled;
  private boolean rounded = false;
  private boolean isTab = false;
  private boolean isRadio = false;
  private boolean isActive = false;
  private ActionListener switchPanelAction;
  private Color prevColor;
  private static ArrayList<Button> links = new ArrayList<>();

  public Button(int x, int y, String text, Color color) {
    super(text);
    drawDefaultStyle();
    setColor(color);
    setPosition(x, y);
    setBackground(color);
    setDark(true);
    filled = true;
  }

  public Button(int x, int y, String text) {
    super(text);
    drawDefaultStyle();
    setPosition(x, y);
    setBackground(MasterUI.primaryCol);
    setDark(true);
    setContentAreaFilled(false);
    filled = false;
  }

  public Button(int x, int y, String text, JPanel switchTo) {
    super(text);
    drawDefaultStyle();
    switchPanelAction = HomeUI.switchPanelAction(switchTo);
    addActionListener(switchPanelAction);
    setPosition(x, y);
    setTab();
    setColor(MasterUI.primaryColAlt);
    filled = true;
  }

  /**
   * Change reference of Panel to which the button should link to
   * 
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
   * Get whether button is active
   * 
   * @return Boolean value of active button
   */
  public boolean getActive() {
    return isActive;
  }

  /**
   * Set whether button is active
   * 
   * @param isActive - Boolean value of active button
   */
  public void setActive(boolean isActive) {
    this.isActive = isActive;
  }

  /**
   * Toggle current active state
   */
  public void toggleActive() {
    isActive = !isActive;
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
   * Get whether button is set on dark mode
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
   * @param dark - Boolean <code>true</code> if dark mode should be enabled
   */
  public void setDark(boolean dark) {
    if (getDark() == dark)
      return;
    this.dark = dark;
    if (dark)
      setForeground(Color.WHITE);
    else
      setForeground(Color.BLACK);
  }

  /**
   * Get previously set color
   * 
   * @return Color
   */
  public Color getPrevColor() {
    return this.prevColor;
  }

  /**
   * Store current color, to be accessed later
   * 
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
   * Transform button to tab button. Tabs are wider and uncolored. Mainly used for
   * panel navigation.
   */
  public void setTab() {
    if (getTab())
      return;
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
   * Transform button to small button. Small button are uncolored.
   */
  public void setSmall() {
    setMargin(new Insets(0, 0, 0, 0));
    setDark(false);
    setColor(MasterUI.lightCol);
    setSize(70, 40);
  }

  /**
   * Get whether button is a tab button
   * 
   * @return boolean is tab or not
   */
  public boolean getTab() {
    return isTab;
  }

  /**
   * Center text inside button
   */
  public void centerText() {
    setVerticalTextPosition(SwingConstants.BOTTOM);
    setHorizontalAlignment(SwingConstants.CENTER);
    setVerticalAlignment(SwingConstants.CENTER);
  }

  /**
   * Get whether button is rounded
   * 
   * @return Boolean value of rounded
   */
  public boolean getRounded() {
    return rounded;
  }

  /**
   * Set whether button is rounded
   * 
   * @param value - Boolean value to be set of rounded
   */
  public void setRounded(boolean value) {
    if (getRounded() == value)
      return;
    setOpaque(!value);
    rounded = value;
    repaint();
  }

  /**
   * Get whether button is a radio button
   * 
   * @return
   */
  public boolean getRadio() {
    return isRadio;
  }

  private void radioToggleAction() {
    if (getActive()) {
      setRounded(false);
      setOpaque(false);
      setEnabled(true);
      repaint();
    } else {
      setRounded(true);
      setEnabled(false);
    }
    toggleActive();

  }
  
  private ActionListener radioAction = e -> { 
    radioToggleAction();
    for (Button link : links) {
      if (e.getSource() != link){
        link.setActive(true);
        link.radioToggleAction();
      }
    }
  };

  /**
   * Set whether button is a radio button
   * 
   * @param isRadio - Boolean value to set to isRadio
   */
  public void setRadio(boolean isRadio) {
    if (getRadio() == isRadio)
      return;
    setOpaque(!isRadio);
    setSize(13, 13);
    this.isRadio = isRadio;
    repaint();
    if (isActive) {
      setRounded(true);
      setEnabled(false);
    }
    addActionListener(radioAction);
  }

  public static Button createRadioButton(int x, int y, String optionTitle, boolean active, Panel panel) {
    Button radioBtn = new Button(x, y, "", MasterUI.secondaryCol);
    radioBtn.setActive(active);
    radioBtn.setRadio(true);
    links.add(radioBtn);
    Label radioLabel = new Label(optionTitle, radioBtn);
    radioLabel.setFont(MasterUI.robotoFont.deriveFont(12f));
    radioLabel.setForeground(Color.WHITE);
    radioLabel.setUnset(true);
    panel.add(radioLabel);
    panel.add(radioBtn);

    return radioBtn;
  }

  /**
   * Remove all action listener of a button
   */
  public void wipeAllActionListener() {
    ActionListener[] actions = getActionListeners();
    for (ActionListener action : actions) {
      removeActionListener(action);
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setColor(getBackground());
    if (getRounded()) {
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
    } else if (getRadio()) {
      g2d.setColor(Color.WHITE);
      g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2d.drawRoundRect(0, 0, getWidth() - 1, getWidth() - 1, 50, 50);
    }
    super.paintComponent(g2d);
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

  public void mouseReleased(MouseEvent e) {
  }

  public void mouseClicked(MouseEvent e) {
  }

}