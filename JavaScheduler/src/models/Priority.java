package models;

import views.MasterUI;

import javax.swing.*;
import java.awt.*;

/**
 * The Priority Enum is used to set the priority of an event (LOW, MEDIUM, HIGH)
 */
public enum Priority {
  HIGH("High", MasterUI.hiPrioCol, MasterUI.hiPrioIcon), 
  MEDIUM("Medium", MasterUI.midPrioCol, MasterUI.midPrioIcon), 
  LOW("Low", MasterUI.loPrioCol, MasterUI.loPrioIcon);

  /** Name of priority */
  private String name;
  /** Color of priority */
  private Color color;
  /** Icon of priority */
  private ImageIcon icon;

  /**
   * Constructor for priority enum
   * @param name - name of priority
   * @param color - color of priority
   * @param icon - icon of priority
   */
  private Priority(String name, Color color, ImageIcon icon){
    this.name = name;
    this.color = color;
    this.icon = icon;
  }

  /**
   * Get the name of the priority as string
   * @return name of the priority as string
   */
  public String toString() {
    return name;
  }

  /**
   * Get Color of the priority
   * @return the color according to priority(green,yellow,red)
   */
  public Color getColor() {
    return color;
  }

  /**
   * Get icon of priority
   * @return the icon of priority
   */
  public ImageIcon getIcon() {
    return icon;
  }
}
