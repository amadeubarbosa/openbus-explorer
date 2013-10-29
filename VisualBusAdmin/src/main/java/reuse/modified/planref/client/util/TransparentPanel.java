package reuse.modified.planref.client.util;

import java.awt.LayoutManager;

import javax.swing.JPanel;

/**
 * Painel com background transparente.
 * 
 * @author Tecgraf
 */
public class TransparentPanel extends JPanel {

  /**
   * Construtor.
   */
  public TransparentPanel() {
    super();
    setOpaque(false);
  }

  /**
   * Construtor.
   * 
   * @param layout o layout manager
   * @param isDoubleBuffered flag de doubleBuffered
   */
  public TransparentPanel(LayoutManager layout, boolean isDoubleBuffered) {
    super(layout, isDoubleBuffered);
    setOpaque(false);
  }

  /**
   * Construtor.
   * 
   * @param layout layout manager.
   */
  public TransparentPanel(LayoutManager layout) {
    super(layout);
    setOpaque(false);
  }

  /**
   * Construtor.
   * 
   * @param isDoubleBuffered flag de doubleBuffered.
   */
  public TransparentPanel(boolean isDoubleBuffered) {
    super(isDoubleBuffered);
    setOpaque(false);
  }

}
