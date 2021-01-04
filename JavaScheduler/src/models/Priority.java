package models;

import java.awt.Color;

import javax.swing.ImageIcon;

import views.MasterUI;

public enum Priority {
  HIGH("High", MasterUI.hiPrioCol, MasterUI.hiPrioIcon), 
  MEDIUM("Medium", MasterUI.midPrioCol, MasterUI.midPrioIcon), 
  LOW("Low", MasterUI.loPrioCol, MasterUI.loPrioIcon);

  private String name; 
  private Color color;
  private ImageIcon icon;

  private Priority(String name, Color color, ImageIcon icon){
    this.name = name;
    this.color = color;
    this.icon = icon;
  }

  public String toString() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public ImageIcon getIcon() {
    return icon;
  }
}
