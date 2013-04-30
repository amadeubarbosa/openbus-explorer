package reuse.modified.logistic.client.desktop;

import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import logistic.client.util.UI;
import admin.desktop.BlockableWindow;

/**
 * O glasspane que intercepta os eventos de GUI durante as chamadas remotas
 * síncronas.
 * 
 * Modificado para utilizar BlockableWindow, ao invés de GenericFrame
 */
public class GlassPane extends JPanel implements AWTEventListener {

  /** As regras de transparência para a janela */
  final static private AlphaComposite composite = AlphaComposite.getInstance(
    AlphaComposite.SRC_OVER, 1f);
  /** A cor transparente do glasspane */
  final static private Color color = UI.GLASS_PANE_COLOR;

  /** A janela cujos eventos serão bloqueados pela GlassPane */
  private Window theWindow;
  /** O retângulo utilizado para desenhar a glasspane sobre a janela */
  private Rectangle2D.Double rectangle;

  /**
   * Construtor.
   * 
   * @param theWindow a janela que utilizará a GlassPane
   */
  public GlassPane(final BlockableWindow theWindow) {
    this.rectangle = new Rectangle2D.Double(0, 0, 0, 0);
    setOpaque(false);

    addKeyListener(new KeyAdapter() {
    });

    this.theWindow = theWindow;
  }

  /**
   * Intercepta todos os eventos de teclado do AWT e consome aqueles que
   * originaram da janela utilizando o glassPane
   * 
   * @param event o evento AWTEvent capturado
   */
  @Override
  public void eventDispatched(AWTEvent event) {
    Object source = event.getSource();

    // descarta o evento se sua origem não for do tipo Component
    boolean sourceIsComponent = (event.getSource() instanceof Component);

    if ((event instanceof KeyEvent) && sourceIsComponent) {
      if (SwingUtilities.windowForComponent((Component) source) == theWindow) {
        ((KeyEvent) event).consume();
      }
    }
  }

  /**
   * Ativa ou desativa a GlassPane. Quando ativada, o cursor do mouse vira uma
   * ampulheta ao passar por cima da GlassPane.
   * 
   * @param value verdadeiro para ativar a glasspane, falso para desativar
   */
  @Override
  public void setVisible(boolean value) {
    if (value) {
      Toolkit.getDefaultToolkit().addAWTEventListener(this,
        AWTEvent.KEY_EVENT_MASK);
      super.setVisible(value);
    }
    else {
      Toolkit.getDefaultToolkit().removeAWTEventListener(this);

      super.setVisible(value);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    g2d.setComposite(composite);
    g2d.setColor(color);
    rectangle.width = getRootPane().getWidth();
    rectangle.height = getRootPane().getHeight();
    g2d.fill(rectangle);
  }
}
