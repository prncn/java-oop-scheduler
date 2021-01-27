package views.components;

import java.awt.Color;

public class DataButton<T> extends Button {
  private static final long serialVersionUID = 1L;
  private T data;

  public DataButton(int x, int y, String text, Color color) {
    super(x, y, text, color);
  }

  public void bind(T data) {
    this.data = data;
  }

  public T getData() {
    return data;
  }
}