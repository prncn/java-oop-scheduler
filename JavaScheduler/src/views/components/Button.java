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

public class Button extends JButton implements MouseListener{

  private static final long serialVersionUID = 1L;
  private Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
  private Color color;
  private int width = 100;
  private int height = 40;
  private Boolean isFilled;
  private Boolean isTab;

  public Button(int x, int y, String text, Color color) {
    super(text);
    this.color = color;
    this.setCursor(cursor);
    this.setBounds(x, y, width, height);
    this.setContentAreaFilled(false);
    this.setBorderPainted(false);
    this.setBackground(color);
    isTab = false;
    isFilled = true;

    addMouseListener(this);
  }
  
  public Button(int x, int y, String text) {
    super(text);
    this.setCursor(cursor);
    this.setBounds(x, y, width, height);
    this.setBorderPainted(false);
    this.setBackground(MasterUI.getColor("primaryCol"));
    isTab = false;
    isFilled = false;

    addMouseListener(this);
  }

  public Button(int x, int y, String text, JPanel switchTo) {
    super(text);
    this.color = MasterUI.getColor("primaryColAlt");
    this.setCursor(cursor);
    this.setBounds(x, y, width, height);
    this.setBorderPainted(false);
    this.setBackground(MasterUI.getColor("primaryColAlt"));
    this.setSize(200, 50);
    this.setHorizontalAlignment(SwingConstants.LEFT);
    this.setMargin(new Insets(10, 10, 10, 10));
    this.setFont(this.getFont().deriveFont(13f));
    this.setForeground(Color.WHITE);
    this.setFocusPainted(false);
    this.setIconTextGap(15);
    isTab = true;
    isFilled = true;

    this.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HomeUI.switchPanel(switchTo);
      }
    });

    addMouseListener(this);
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
    isTab = true;
  }

  public Boolean getIsTab() {
    return this.isTab;
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
  
  public void mousePressed(MouseEvent e) {
    this.setBackground(color);
  }
  public void mouseReleased(MouseEvent e) {}
  public void mouseClicked(MouseEvent e) {}

}
