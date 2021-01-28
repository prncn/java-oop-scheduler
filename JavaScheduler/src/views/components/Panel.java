package views.components;

import views.MasterUI;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.RenderingHints;


public class Panel extends JPanel {

  private static final long serialVersionUID = -3799733211815510931L;
  public boolean isActive;
  public boolean isCard;
  public boolean rounded;
  private float alpha = 1f;

  public Panel() {
    super();
    this.setLayout(null);
  }

  public Panel(JFrame frame) {
    this.setBounds(200, 0, frame.getWidth() - 200, frame.getHeight());
    this.setBackground(MasterUI.lightCol);
    this.setLayout(null);
  }

  /**
   * Adjust panel bounds to changed frame 
   * @param frame - Changed frame
   */
  public void updateBounds(JFrame frame) {
    this.setBounds(0, 0, frame.getWidth(), frame.getHeight());
  }

  
  /**
   * Get transparency level of panel
   * @return Alpha level
   */
  public float getAlpha() {
    return alpha;
  }

  /**
   * Get whether panel is set to be rounded
   * @return
   */
  public boolean getRounded() {
    return rounded;
  }

  public boolean getCard() {
    return isCard;
  }

  /**
   * Set panel to be rounded or not. Rounds corners of panel.
   * @param value
   */
  public void setRounded(boolean value){
    if(rounded != value){
      setOpaque(!value);
      rounded = value;
      repaint();
    }
  }

  public void setCard(boolean value){
    if(isCard != value){
      setOpaque(!value);
      isCard = value;
      repaint();
    }
  }
  
  /**
   * Set transparency level of panel
   * @param value - Alpha value to be set
   */
  public void setAlpha(float value) {
    if (alpha != value) {
      alpha = Math.min(Math.max(0f, value), 1f);
      setOpaque(alpha == 1.0f);
      repaint();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setComposite(AlphaComposite.SrcOver.derive(getAlpha()));
    g2d.setColor(getBackground());
    if(rounded){
      g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
    } else {
      g2d.fillRect(0, 0, getWidth(), getHeight());
    }
   
    g2d.dispose();
  }
}
