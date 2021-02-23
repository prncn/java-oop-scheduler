package views.components;

import java.awt.Color;

/**
 * DataButton inherits from Button class. The purpose of this class
 * is to bind some class data to the button. Example, a User can be bound
 * to a DataButton, and then be accessed directly from that button. 
 */
public class DataButton<T> extends Button {

  /** Default serial ID */
  private static final long serialVersionUID = 1L;

  /** Generic data object to be bound to button */
  private T data;

  /**
   * Constructor for DataButton
   * @param x - X position of button
   * @param y - Y position of button
   * @param text - Text inside button
   * @param color - Color of button
   */
  public DataButton(int x, int y, String text, Color color) {
    super(x, y, text, color);
  }

  /**
   * Bind some generic data (such as a User or Location)
   * to this button.
   * @param data - Generic data object
   */
  public void bind(T data) {
    this.data = data;
  }

  /**
   * Get the data that is stored in button
   * @return Generic data object
   */
  public T getData() {
    return data;
  }
}