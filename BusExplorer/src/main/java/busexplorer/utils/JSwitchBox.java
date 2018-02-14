package busexplorer.utils;

import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.JLabel;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Alternative implementation of a checkbox using fancy on/off switches drawed with arched-border rectangles manually.
 * <p>Examples:
 *   <img src="../../../../../img/off.jpg" alt="example of JSwitchBox deselected">
 *   <img src="../../../../../img/on.jpg" alt="example of JSwitchBox selected">
 * <p>
 * {@link JSwitchBox} can be instantiate using custom on/off labels and colors (for both text and background) but
 * other attributes as gap and dimension will be calculated. Font are the same that {@link JLabel#getFont()} and gap
 * is calculated using {@link JLabel#getFontMetrics(Font)} and is dependent of the bigger label size.
 * <p>
 * Original code was authored by <a href='https://stackoverflow.com/users/307767/oliholz'>oliholz</a> at
 * <a href='https://stackoverflow.com/questions/7304017/how-to-add-sexy-on-off-sliders'>stackoverflow.com question</a>.
 * Some improvements are given since:
 * <ul>
 * <li>rectangles exceed component area and left some visual garbage behind (fix: vertical height reduction to 16px)</li>
 * <li>setEnabled(false) has no effect and button continues flipping (fix: check isEnabled() on MouseListener)</li>
 * <li>custom background and text colors (fix: optional constructors)</li>
 * <li>builtin support for text colors through UIManager (fix: reusing the CheckBox foreground and disabledText)</li>
 * </ul>
 * @author Tecgraf/PUC-Rio
 *
 **/
public class JSwitchBox extends AbstractButton {
  private final String trueLabel;
  private final String falseLabel;
  protected Color foregroundText = UIManager.getColor("CheckBox.foreground");
  protected Color disabledText = UIManager.getColor("CheckBox.disabledText");
  protected Color off = new Color(200, 200, 200);
  protected Color on = new Color(0, 106, 215);
  private Color colorBright = new Color(220, 220, 220);
  private Color colorDark = new Color(100, 100, 100);
  private Color black = new Color(0, 0, 0);
  private Color white = new Color(255, 255, 255, 100);
  private Color light = new Color(220, 220, 220, 100);
  private Font font = new JLabel().getFont();
  private int gap = 5;
  private int globalWitdh = 0;
  private Dimension thumbBounds;
  private int max;

  /**
   * Creates a switch box with custom text for both states and the colors are defined by default values as following:
   * {@link JSwitchBox#on}, {@link JSwitchBox#off}, {@link JSwitchBox#foregroundText} and {@link JSwitchBox#disabledText}.
   * <p>
   * {@link JSwitchBox#foregroundText} by default is defined as {@code CheckBox.foreground} on {@link UIManager#getColor}.
   * {@link JSwitchBox#disabledText} by default is defined as {@code CheckBox.disabledText} on {@link UIManager#getColor}.
   *
   * @param trueLabel text showed in activated state
   * @param falseLabel text showed in deactivated state
   */
  public JSwitchBox(String trueLabel, String falseLabel) {
    this.trueLabel = trueLabel;
    this.falseLabel = falseLabel;
    buildComponent();
  }

  /**
   * Creates a switch box with custom text and background colors for both states, only text color will use the default values.
   *
   * @param trueLabel text showed in activated state
   * @param falseLabel text showed in deactivated state
   * @param on background color used in activated state
   * @param off background color used in deactivated state
   */
  public JSwitchBox(String trueLabel, String falseLabel, Color on, Color off) {
    this.trueLabel = trueLabel;
    this.falseLabel = falseLabel;
    this.on = on;
    this.off = off;
    buildComponent();
  }

  /**
   * Creates a switch box with custom text and the background colors for both states, text color also can be overrided.
   *
   * @param trueLabel text showed in activated state
   * @param falseLabel text showed in deactivated state
   * @param on background color used in activated state
   * @param off background color used in deactivated state
   * @param foregroundText text color when the component is enabled
   * @param disabledText text color when the component is disabled
   */
  public JSwitchBox(String trueLabel, String falseLabel, Color on, Color off, Color foregroundText,
    Color disabledText) {
    this.trueLabel = trueLabel;
    this.falseLabel = falseLabel;
    this.on = on;
    this.off = off;
    this.foregroundText = foregroundText;
    this.disabledText = disabledText;
    buildComponent();
  }

  private void buildComponent() {
    double trueLenght = getFontMetrics(getFont()).getStringBounds(trueLabel, getGraphics()).getWidth();
    double falseLenght = getFontMetrics(getFont()).getStringBounds(falseLabel, getGraphics()).getWidth();
    max = (int) Math.max(trueLenght, falseLenght);
    gap = Math.min(5, 5 + (int) Math.abs(trueLenght - falseLenght));
    thumbBounds = new Dimension(max + gap * 2, 16);
    globalWitdh = max + thumbBounds.width + gap * 2;
    setModel(new DefaultButtonModel());
    setSelected(false);
    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        Rectangle area = new Rectangle(getPreferredSize());
        if (JSwitchBox.this.isEnabled() && area.contains(e.getPoint())) {
          setSelected(!isSelected());
        }
      }
    });
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(globalWitdh, thumbBounds.height);
  }

  @Override
  public void setSelected(boolean b) {
    if (b) {
      setText(trueLabel);
      setBackground(on);
    }
    else {
      setBackground(off);
      setText(falseLabel);
    }
    super.setSelected(b);
  }

  @Override
  public void setText(String text) {
    super.setText(text);
  }

  @Override
  public int getHeight() {
    return getPreferredSize().height;
  }

  @Override
  public int getWidth() {
    return getPreferredSize().width;
  }

  @Override
  public Font getFont() {
    return font;
  }

  @Override
  protected void paintComponent(Graphics g) {
    g.setColor(getBackground());
    g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 2, 2);
    Graphics2D g2 = (Graphics2D) g;

    g2.setColor(black);
    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 2, 2);

    int x = 0;
    int lx = 0;
    if (isSelected()) {
      lx = thumbBounds.width;
    }
    else {
      x = thumbBounds.width;
    }
    int y = 0;
    int w = thumbBounds.width;
    int h = thumbBounds.height;

    g2.setPaint(new GradientPaint(x, (int) (y - 0.1 * h), colorDark, x, (int) (y + 1.2 * h), white));
    g2.fillRect(x, y, w, h);
    g2.setPaint(new GradientPaint(x, (int) (y + .65 * h), light, x, (int) (y + 1.3 * h), colorDark));
    g2.fillRect(x, (int) (y + .65 * h), w, (int) (h - .65 * h));

    if (w > 14) {
      int size = 10;
      g2.setColor(colorBright);
      g2.fillRect(x + w / 2 - size / 2, y + h / 2 - size / 2, size, size);
      g2.setColor(new Color(120, 120, 120));
      g2.fillRect(x + w / 2 - 4, h / 2 - 4, 2, 2);
      g2.fillRect(x + w / 2 - 1, h / 2 - 4, 2, 2);
      g2.fillRect(x + w / 2 + 2, h / 2 - 4, 2, 2);
      g2.setColor(colorDark);
      g2.fillRect(x + w / 2 - 4, h / 2 - 2, 2, 6);
      g2.fillRect(x + w / 2 - 1, h / 2 - 2, 2, 6);
      g2.fillRect(x + w / 2 + 2, h / 2 - 2, 2, 6);
      g2.setColor(new Color(170, 170, 170));
      g2.fillRect(x + w / 2 - 4, h / 2 + 2, 2, 2);
      g2.fillRect(x + w / 2 - 1, h / 2 + 2, 2, 2);
      g2.fillRect(x + w / 2 + 2, h / 2 + 2, 2, 2);
    }

    g2.setColor(black);
    g2.drawRoundRect(x, y, w - 1, h - 1, 2, 2);
    g2.setColor(white);
    g2.drawRoundRect(x + 1, y + 1, w - 3, h - 3, 2, 2);

    if (isEnabled()) {
      g2.setColor(this.foregroundText);
    }
    else {
      g2.setColor(this.disabledText);
    }
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2.setFont(getFont());
    g2.drawString(getText(), lx + gap, y + h / 2 + h / 4);
  }
}