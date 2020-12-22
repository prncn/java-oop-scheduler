package views;

import views.components.*;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;

abstract public class MasterUI extends JFrame {
  private static final long serialVersionUID = 349351600837986896L;
  
  /**
   * Global UI components
   */
  protected static JPanel panel = new JPanel();
  protected static Label userWelcome = new Label();

  /**
   * Global colors and fonts
   */
  private static Boolean LIGHT_MODE = false;
  protected static Color primaryCol = new Color(50, 50, 65);
  protected static Color primaryColAlt = new Color(60, 60, 75);
  protected static Color foregroundCol = Color.WHITE;
  // protected static Color primaryCol = new Color(250, 250, 255);
  // protected static Color primaryColAlt = new Color(240, 240, 245);
  // protected static Color foregroundCol = Color.BLACK;

  protected static Color accentCol = new Color(116, 207, 183);
  protected static Color accentColDark = accentCol.darker();
  protected static Font bodyFont; // has to be wrapped in try catch
  protected static Font bodyFontAlt = new Font("Arial", Font.BOLD, 15);
  protected static Font monoFont = new Font("Consolas", Font.PLAIN, 15);

  /**
   * File path and images
   */
  private static File fileRoot = new File(System.getProperty("user.dir"));
  private static String iconsRoot = "/JavaScheduler/assets/icons/";
  protected static ImageIcon favicon = new ImageIcon(fileRoot + iconsRoot + "favicon-96x96.png");
  protected static ImageIcon loginHeroImage = new ImageIcon(
    fileRoot + iconsRoot + "undraw_Analysis_re_w2vd.png");
  protected static ImageIcon signupHeroImage = new ImageIcon(
    fileRoot + iconsRoot + "undraw_Cloud_docs_re_xjht.png");
  protected static ImageIcon dashboardIcon = new ImageIcon(
    fileRoot + iconsRoot + "dashboard-solid-24.png");
  protected static ImageIcon createMeetingIcon = new ImageIcon(
    fileRoot + iconsRoot + "add-to-queue-solid-24.png");
  protected static ImageIcon exportIcon = new ImageIcon(
    fileRoot + iconsRoot + "download-solid-24.png");
  protected static ImageIcon calendarIcon = new ImageIcon(
    fileRoot + iconsRoot + "calendar-regular-24.png");
  protected static ImageIcon settingsIcon = new ImageIcon(
    fileRoot + iconsRoot + "category-regular-24.png");
  protected static ImageIcon logoutIcon = new ImageIcon(
    fileRoot + iconsRoot + "log-out-solid-24.png");

  public MasterUI() {

    this.setIconImage(favicon.getImage());
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLayout(null);
    
    panel.setBackground(primaryCol);
    panel.setLayout(null);
    
    this.add(panel);
    try {
      bodyFont = Font
          .createFont(Font.TRUETYPE_FONT, new File(fileRoot + "/JavaScheduler/assets/fonts/UniversLTStd.otf"))
          .deriveFont(15f);
    } catch (IOException | FontFormatException e) {
      System.out.println(e);
      bodyFont = bodyFontAlt; // if font asset import failed, fall back to Arial
    }
  }
  
  /**
   * Getter for external or non-inherited classes
   * @return Color from MasterUI
   */
  public static Color getColor(String color) {
    switch(color){
      case "primaryCol": return primaryCol;
      case "primaryColAlt": return primaryColAlt;
      case "accentCol": return accentCol;
      default: throw new IllegalArgumentException("Invalid color");
    }
  }

  /**
   * Loop through each UI component and change its style depending on Swing
   * instance.
   */
  public void setComponentStyles(JPanel panel) {
    for(Component p : this.getRootPane().getComponents()){
      if(p instanceof JPanel){
        // System.out.println("Panel found..");
      }
    }
    for(Component c : panel.getComponents()){
      if(c instanceof JLabel || c instanceof JTextField || c instanceof JButton){
        c.setFont(monoFont);
        c.setForeground(foregroundCol);
      }
      if(c instanceof JTextField){
        c.setFont(monoFont.deriveFont(19f));
      }
      if(c instanceof JLabel){
        c.setFont(bodyFont);
      }
      if(c instanceof JButton){
        if(((Button) c).getIsTab()) {
          c.setFont(bodyFont);
        }
        ((AbstractButton) c).setFocusPainted(false);
        ((AbstractButton) c).setContentAreaFilled(true);
        ((AbstractButton) c).setMargin(new Insets(5, 5, 3, 3));
      }
      if(c instanceof JTextField){
        ((JTextComponent) c).setCaretColor(foregroundCol);
        ((JTextComponent) c).setBackground(primaryColAlt);
        ((JTextComponent) c).setBorder(javax.swing.BorderFactory.createEmptyBorder());
      }
    }
  }

}