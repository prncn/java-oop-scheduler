package views.components;

import views.MasterUI;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class Panel extends JPanel {

  private static final long serialVersionUID = -3799733211815510931L;
  public boolean isActive;
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

  /**
   * Get transparence level of panel
   * @return Alpha level
   */
  public float getAlpha() {
    return alpha;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setComposite(AlphaComposite.SrcOver.derive(getAlpha()));
    g2d.setColor(getBackground());
    g2d.fillRect(0, 0, getWidth(), getHeight());
    g2d.dispose();

  }
}
