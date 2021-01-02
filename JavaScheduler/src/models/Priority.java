package models;

import java.awt.Color;

import views.MasterUI;

public enum Priority {
  HIGH("High", MasterUI.hiPrioCol), MEDIUM("Medium", MasterUI.midPrioCol), LOW("Low", MasterUI.loPrioCol);

  private String name; 
  private Color color;

  private Priority(String name, Color color){
    this.name = name;
    this.color = color;
  }

  public String toString() {
    return name;
  }

  public Color getColor() {
    return color;
  }
}
