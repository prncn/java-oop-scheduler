package views.components;

import views.MasterUI;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Color;
import javax.swing.JButton;

public class Button extends JButton implements MouseListener{

  private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
  private Color color;
  private int width = 100;
  private int height = 40;
  private Boolean isFilled;

  public Button(int x, int y, String text, Color color) {
    super(text);
    this.color = color;
    this.setCursor(cursor);
    this.setBounds(x, y, width, height);
    this.setBorderPainted(false);
    this.setBackground(color);
    isFilled = true;

    addMouseListener(this);
  }
  
  public Button(int x, int y, String text) {
    super(text);
    this.setCursor(cursor);
    this.setBounds(x, y, width, height);
    this.setBorderPainted(false);
    this.setBackground(MasterUI.getPrimaryCol());
    isFilled = false;

    addMouseListener(this);
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    if(this.isFilled){
      this.setBackground(color.darker());
    }
  }

  @Override
  public void mouseExited(MouseEvent e) {
    if(this.isFilled){
      this.setBackground(color);
    }
  }
  
  public void mousePressed(MouseEvent e) {}
  public void mouseReleased(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}

}
