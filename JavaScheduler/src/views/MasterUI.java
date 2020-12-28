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

import java.io.File;
import java.io.IOException;

abstract public class MasterUI extends JFrame {
  private static final long serialVersionUID = 349351600837986896L;

  /**
   * Global UI components
   */
  protected static Panel panel = new Panel();
  protected static Label userWelcome = new Label();

  /**
   * Global colors and fonts
   */
  // private static Boolean LIGHT_MODE = false;
  protected static Color primaryCol = new Color(55, 55, 70);
  protected static Color primaryColAlt = new Color(60, 60, 75);
  protected static Color fontCol = Color.WHITE;
  protected static Color lightCol = new Color(250, 250, 255);
  protected static Color lightColAlt = new Color(240, 240, 245);
  // protected static Color primaryCol = new Color(250, 250, 255);
  // protected static Color primaryColAlt = new Color(240, 240, 245);
  // protected static Color foregroundCol = Color.BLACK;

  protected static Color secondaryCol = new Color(116, 207, 183);
  protected static Color secondaryColAlt = secondaryCol.darker();
  protected static Color accentCol = new Color(102, 0, 255);

  protected static Font bodyFont; // has to be wrapped in try catch
  protected static Font bodyFontAlt = new Font("Arial", Font.BOLD, 15);
  protected static Font monoFont = new Font("Consolas", Font.PLAIN, 15);

  /**
   * File path and images
   */
  private static File fileRoot = new File(System.getProperty("user.dir"));
  private static String iconsRoot = "/JavaScheduler/assets/icons/";
  private static String imagesRoot = "/JavaScheduler/assets/images/";

  protected static ImageIcon loginHeroImage = new ImageIcon(fileRoot + imagesRoot + "undraw_Analysis_re_w2vd.png");
  protected static ImageIcon signupHeroImage = new ImageIcon(fileRoot + imagesRoot + "undraw_Cloud_docs_re_xjht.png");
  protected static ImageIcon createdMeetingImage = new ImageIcon(
      fileRoot + imagesRoot + "undraw_relaxing_walk_mljx.png");

  protected static ImageIcon favicon = new ImageIcon(fileRoot + iconsRoot + "category-solid-24.png");
  protected static ImageIcon adminIcon = new ImageIcon(fileRoot + iconsRoot + "menu-alt-left-regular-24.png");
  protected static ImageIcon nextIcon = new ImageIcon(fileRoot + iconsRoot + "chevron-right-solid-24.png");
  protected static ImageIcon prevIcon = new ImageIcon(fileRoot + iconsRoot + "chevron-left-solid-24.png");
  protected static ImageIcon backIcon = new ImageIcon(fileRoot + iconsRoot + "left-arrow-alt-solid-24.png");
  protected static ImageIcon downIcon = new ImageIcon(fileRoot + iconsRoot + "chevron-down-solid-24.png");
  protected static ImageIcon dashboardIcon = new ImageIcon(fileRoot + iconsRoot + "category-regular-24.png");
  protected static ImageIcon addUserIcon = new ImageIcon(fileRoot + iconsRoot + "user-plus-solid-24.png");
  protected static ImageIcon circleUserIcon = new ImageIcon(fileRoot + iconsRoot + "user-circle-regular-36.png");
  protected static ImageIcon createMeetingIcon = new ImageIcon(fileRoot + iconsRoot + "add-to-queue-solid-24.png");
  protected static ImageIcon exportIcon = new ImageIcon(fileRoot + iconsRoot + "download-solid-24.png");
  protected static ImageIcon calendarIcon = new ImageIcon(fileRoot + iconsRoot + "calendar-regular-24.png");
  protected static ImageIcon profileIcon = new ImageIcon(fileRoot + iconsRoot + "user-solid-24.png");
  protected static ImageIcon logoutIcon = new ImageIcon(fileRoot + iconsRoot + "log-out-solid-24.png");

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
   * 
   * @return Color from MasterUI
   */
  public static Color getColor(String color) {
    switch (color) {
      case "primaryCol":
        return primaryCol;
      case "primaryColAlt":
        return primaryColAlt;
      case "secondaryCol":
        return secondaryCol;
      case "accentCol":
        return accentCol;
      case "lightCol":
        return lightCol;
      case "lightColAlt":
        return lightColAlt;
      default:
        throw new IllegalArgumentException("Invalid color name");
    }
  }

  /**
   * Getter for external or non-inherited classes
   * 
   * @return Color from MasterUI
   */
  public static Font getFont(String font) {
    switch (font) {
      case "bodyFont":
        return bodyFont;
      case "monoFont":
        return monoFont;
      default:
        throw new IllegalArgumentException("Invalid font name");
    }
  }

  /**
   * Set foreground Color
   * 
   * @param color - Color to be set
   */
  public static void setForegroundCol(Color color) {
    fontCol = color;
  }

  /**
   * Loop through components of the frame and change their style properties
   * 
   * @param panel     - Selcted panel to apply the changes to
   * @param colorMode - String either "light" or "dark", changes color mode. Null defaults to dark.
   */
  public void setComponentStyles(JPanel panel, String colorMode) {
    Color foreground;
    Color background;
    if (colorMode == "dark" || colorMode == null) {
      foreground = fontCol;
      background = primaryColAlt;
    } else if (colorMode == "light") {
      foreground = primaryColAlt;
      background = new Color(240, 240, 245);
    } else {
      throw new IllegalArgumentException("Invalid color mode.");
    }

    for (Component c : panel.getComponents()) {
      if (c instanceof JLabel || c instanceof JTextField) {
        c.setFont(monoFont);
        c.setForeground(foreground);
      }
      if (c instanceof JTextField) {
        c.setFont(monoFont.deriveFont(19f));
      }
      if (c instanceof JLabel) {
        c.setFont(bodyFont);
      }
      if (c instanceof JButton) {
        c.setFont(monoFont);
        if (((Button) c).getTab()) {
          c.setFont(bodyFont);
        }
        if (((Button) c).getDark()) {
          ((AbstractButton) c).setForeground(fontCol);
        }
        ((AbstractButton) c).setFocusPainted(false);
        ((AbstractButton) c).setContentAreaFilled(true);
        ((AbstractButton) c).setMargin(new Insets(5, 5, 3, 3));
      }
      if (c instanceof JTextField) {
        ((JTextComponent) c).setCaretColor(foreground);
        ((JTextComponent) c).setBackground(background);
        ((JTextComponent) c).setBorder(javax.swing.BorderFactory.createEmptyBorder());
      }
    }
  }

}