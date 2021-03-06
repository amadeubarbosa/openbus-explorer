package busexplorer.panel;

import java.awt.LayoutManager;
import javax.swing.JPanel;

/**
 * Extens�o do JPanel para permitir um painel receber notifica��es de 'refresh'
 * e ent�o notificar seus constituintes para preparar os recursos externos necess�rios.
 *
 * @author Tecgraf
 */
public abstract class RefreshablePanel extends JPanel implements RefreshDelegate {
  public RefreshablePanel() {
    super();
  }
  public RefreshablePanel(LayoutManager layout) {
    super(layout);
  }
}
