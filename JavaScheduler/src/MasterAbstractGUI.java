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
import java.awt.Cursor;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.File;
import java.io.IOException;

abstract class MasterAbstractGUI extends JFrame {
  private static final long serialVersionUID = 349351600837986896L;
  
  /**
   * Global UI components
   */
  protected static JPanel panel = new JPanel();
  protected static JLabel userWelcome = new JLabel();

  /**
   * Global colors and fonts
   */
  protected static Color primaryCol = new Color(50, 50, 65);
  protected static Color primaryColAlt = new Color(60, 60, 75);
  protected static Color foregroundCol = Color.WHITE;
  // protected static Color primaryCol = new Color(250, 250, 255);
  // protected static Color primaryColAlt = new Color(240, 240, 245);
  // protected static Color foregroundCol = Color.BLACK;
  protected static Color accentCol = new Color(116, 207, 183);
  protected static Color accentColDark = new Color(86, 156, 137);
  protected static Font bodyFont; // has to be wrapped in try catch
  protected static Font bodyFontAlt = new Font("Arial", Font.BOLD, 15);
  protected static Font monoFont = new Font("Consolas", Font.PLAIN, 15);

  /**
   * File path and images
   */
  protected static File fileRoot = new File(System.getProperty("user.dir"));
  protected static ImageIcon favicon = new ImageIcon(fileRoot + "/JavaScheduler/assets/icons/favicon-96x96.png");
  protected static ImageIcon loginHeroImage = new ImageIcon(
      fileRoot + "/JavaScheduler/assets/icons/undraw_Analysis_re_w2vd.png");
  protected static ImageIcon signupHeroImage = new ImageIcon(
    fileRoot + "/JavaScheduler/assets/icons/undraw_Cloud_docs_re_xjht.png");

  public MasterAbstractGUI() {
    this.setIconImage(favicon.getImage());
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.add(panel);

    panel.setBackground(primaryCol);
    panel.setLayout(null);

    try {
      bodyFont = Font
          .createFont(Font.TRUETYPE_FONT, new File(fileRoot + "/JavaScheduler/assets/fonts/UniversLTStd.otf"))
          .deriveFont(15f);
    } catch (IOException | FontFormatException e) {
      System.out.println(e);
      bodyFont = bodyFontAlt; // if font asset import failed, fall back to Arial
    }
  }

  public static JLabel createLabel(int x, int y, String text) {
    JLabel label = new JLabel(text);
    label.setBounds(x, y, 250, 25);
    return label;
  }

  public static JButton createButton(int x, int y, String text, Color color) {
    JButton button = new JButton(text);
    Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
    button.setBounds(x, y, 100, 40);
    button.setBorderPainted(false);
    button.setBackground(color);
    button.setCursor(cursor);

    if(color == accentCol){
      button.addMouseListener(new MouseAdapter() {
        public void mouseEntered(MouseEvent e) {
          button.setBackground(accentColDark);
        }
        public void mouseExited(MouseEvent e) {
          button.setBackground(accentCol);
        }
      });
    }

    return button;
  }

  /**
   * Loop through each UI component and change its style depending on Swing
   * instance.
   */
  public static void setComponentStyles() {
    for (Component c : panel.getComponents()) {
      if(c instanceof JLabel || c instanceof JTextField || c instanceof JButton) {
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