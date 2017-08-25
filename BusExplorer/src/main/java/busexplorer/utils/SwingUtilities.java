package busexplorer.utils;

import javax.swing.JComponent;
import java.awt.Dimension;

public class SwingUtilities {
  /**
   * Ajuste de um conjunto de elementos de interface para o mesmo tamanho.
   *
   * @param comps os widgets que serão ajustados.
   */
  public static void equalizeComponentSize(JComponent... comps) {
    final Dimension dim = new Dimension(0, 0);
    for (JComponent comp : comps) {
      final Dimension pref = comp.getPreferredSize();
      final double h = Math.max(dim.getHeight(), pref.getHeight());
      final double w = Math.max(dim.getWidth(), pref.getWidth());
      dim.setSize(w, h);
    }
    for (JComponent comp : comps) {
      comp.setPreferredSize(dim);
    }
  }

}
