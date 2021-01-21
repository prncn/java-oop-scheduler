package views;

import views.components.Label;
import views.components.Button;
import views.components.Panel;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
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

  /**
   * Global colors and fonts
   */
  public static Color primaryCol = new Color(55, 55, 70);
  public static Color primaryColAlt = new Color(60, 60, 75);
  public static Color fontCol = Color.WHITE;
  public static Color lightCol = new Color(250, 250, 255);
  public static Color lightColAlt = new Color(240, 240, 245);

  public static Color secondaryCol = new Color(116, 207, 183);
  public static Color secondaryColAlt = secondaryCol.darker();
  public static Color accentCol = new Color(102, 0, 255);

  public static Color hiPrioCol = new Color(194, 21, 73);
  public static Color midPrioCol = new Color(219, 218, 149);
  public static Color loPrioCol = secondaryCol;

  public static Font bodyFont; // has to be wrapped in try catch
  public static Font bodyFontAlt = new Font("Arial", Font.PLAIN, 15);
  public static Font monoFont = new Font("Consolas", Font.PLAIN, 15);
  public static Font robotoFont;

  /**
   * File path and images
   */
  private static File fileRoot = new File(System.getProperty("user.dir"));
  private static String iconsRoot = "/JavaScheduler/assets/icons/";
  private static String imagesRoot = "/JavaScheduler/assets/images/";

  public static ImageIcon loginHeroImage = new ImageIcon(fileRoot + imagesRoot + "undraw_Analysis_re_w2vd.png");
  public static ImageIcon signupHeroImage = new ImageIcon(fileRoot + imagesRoot + "undraw_Cloud_docs_re_xjht.png");
  public static ImageIcon createdMeetingImage = new ImageIcon(fileRoot + imagesRoot + "undraw_relaxing_walk_mljx.png");
  public static ImageIcon dashImage = new ImageIcon(fileRoot + imagesRoot + "undraw_complete_task_u2c3.png");

  public static ImageIcon favicon = new ImageIcon(fileRoot + iconsRoot + "category-solid-36.png");
  public static ImageIcon adminIcon = new ImageIcon(fileRoot + iconsRoot + "menu-alt-left-regular-24.png");
  public static ImageIcon nextIcon = new ImageIcon(fileRoot + iconsRoot + "chevron-right-solid-24.png");
  public static ImageIcon prevIcon = new ImageIcon(fileRoot + iconsRoot + "chevron-left-solid-24.png");
  public static ImageIcon backIcon = new ImageIcon(fileRoot + iconsRoot + "left-arrow-alt-solid-24.png");
  public static ImageIcon downIcon = new ImageIcon(fileRoot + iconsRoot + "chevron-down-solid-24.png");
  public static ImageIcon downIconDark = new ImageIcon(fileRoot + iconsRoot + "chevron-down-solid-24-dark.png");
  public static ImageIcon xIcon = new ImageIcon(fileRoot + iconsRoot + "x-regular-24.png");

  public static ImageIcon dashboardIcon = new ImageIcon(fileRoot + iconsRoot + "category-regular-24.png");
  public static ImageIcon addUserIcon = new ImageIcon(fileRoot + iconsRoot + "user-plus-solid-24.png");
  public static ImageIcon circleUserIcon = new ImageIcon(fileRoot + iconsRoot + "user-circle-regular-36.png");
  public static ImageIcon createMeetingIcon = new ImageIcon(fileRoot + iconsRoot + "add-to-queue-solid-24.png");
  public static ImageIcon exportIcon = new ImageIcon(fileRoot + iconsRoot + "download-solid-24.png");
  public static ImageIcon calendarIcon = new ImageIcon(fileRoot + iconsRoot + "calendar-regular-24.png");
  public static ImageIcon profileIcon = new ImageIcon(fileRoot + iconsRoot + "user-solid-24.png");
  public static ImageIcon logoutIcon = new ImageIcon(fileRoot + iconsRoot + "log-out-solid-24.png");
  public static ImageIcon bellIcon = new ImageIcon(fileRoot + iconsRoot + "bell-solid-24.png");
  public static ImageIcon searchIcon = new ImageIcon(fileRoot + iconsRoot + "search-regular-24.png");
  public static ImageIcon folderIcon = new ImageIcon(fileRoot + iconsRoot + "folder-regular-24.png");
  public static ImageIcon editIcon = new ImageIcon(fileRoot + iconsRoot + "edit-solid-24.png");
  public static ImageIcon removeIcon = new ImageIcon(fileRoot + iconsRoot + "trash-alt-regular-24.png");
  
  public static ImageIcon hiPrioIcon = new ImageIcon(fileRoot + iconsRoot + "circle-solid-24-rd.png");
  public static ImageIcon midPrioIcon = new ImageIcon(fileRoot + iconsRoot + "circle-solid-24-ylw.png");
  public static ImageIcon loPrioIcon = new ImageIcon(fileRoot + iconsRoot + "circle-solid-24-grn.png");
  public static ImageIcon pdfIcon = new ImageIcon(fileRoot + iconsRoot + "file-pdf-solid-48.png");
  public static ImageIcon jpgIcon = new ImageIcon(fileRoot + iconsRoot + "file-jpg-solid-48.png");
  public static ImageIcon pngIcon = new ImageIcon(fileRoot + iconsRoot + "file-png-solid-48.png");

  public MasterUI() {
    setIconImage(favicon.getImage());
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(null);
    panel.setBackground(primaryCol);
    panel.setLayout(null);
    add(panel);

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException ex) {
    }

    try {
      bodyFont = Font
          .createFont(Font.TRUETYPE_FONT, new File(fileRoot + "/JavaScheduler/assets/fonts/UniversLTStd.otf"))
          .deriveFont(13f);

      robotoFont = Font
          .createFont(Font.TRUETYPE_FONT, new File(fileRoot + "/JavaScheduler/assets/fonts/Roboto-Regular.ttf"))
          .deriveFont(13f);
    } catch (IOException | FontFormatException e) {
      System.out.println(e);
      bodyFont = bodyFontAlt; // if font asset import failed, fall back to Arial
      robotoFont = bodyFont;
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
   * @param colorMode - String either "light" or "dark", changes color mode. Null
   *                  defaults to dark.
   */
  public static void setComponentStyles(JPanel panel, String colorMode) {
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
      if (c instanceof JLabel) {
        if(c instanceof Label && !((Label ) c).getUnset())
        c.setFont(robotoFont);
        c.setForeground(foreground);
      }
      if (c instanceof JTextField) {
        c.setFont(robotoFont);
        c.setBackground(background);
        c.setForeground(foreground);
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
      if (c instanceof JPasswordField) {
        ((JTextComponent) c).setCaretColor(foreground);
        ((JTextComponent) c).setBackground(background);
        ((JTextComponent) c).setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
      }

    }
  }

}