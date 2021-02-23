package views.components;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Component;

import views.MasterUI;

/**
 * Custom Label component from JLabel
 */
public class Label extends JLabel {

  /** Default serial ID */
  private static final long serialVersionUID = 1L;

  /** Boolean whether label is a heading (title) */
  private boolean isHeading = false;

  /** Boolean whether label styles should not be styled externally  */
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

  /**
   * Set label to be a heading, or title. 
   * Gives it larger font sizing and weight.
   */
  public void setHeading() {
    isHeading = true;
    setUnset(true);
    setSize(500, 40);
    setFont(getFont().deriveFont(Font.BOLD, 30f));
  }

  /** 
   * Get whether label is a heading
   * 
   * @return boolean isHeading
   */
  public boolean getHeading() {
    return isHeading;
  }


  /** 
   * Set whether label is a heading
   * 
   * @param unset
   */
  public void setUnset(boolean unset) {
    this.unset = unset;
  }

  
  /** 
   * Get whether label is unset.
   * Unset labels are ignored by #setComponentStyles() in 
   * MasterUI, and their styles are not overwritten
   *
   * @return boolean
   */
  public boolean getUnset() {
    return unset;
  }

  
  /** 
   * Proper placement of icon besides text label.
   * Use this for small labels with wrong icon placement.
   * 
   * @param icon - ImageIcon object
   */
  public void appendIcon(ImageIcon icon) {
    setIcon(icon);
    setVerticalTextPosition(SwingConstants.BOTTOM);
    setIconTextGap(10);
  }

  
  /** 
   * Use label component for an image instead of text.
   * This fills out the entire label area with the icon object.
   * 
   * @param icon - ImageIcon object to be set to label
   */
  public void fillIcon(ImageIcon icon) {
    setIcon(icon);
    setSize(getIcon().getIconWidth(), getIcon().getIconHeight());
  }
}
