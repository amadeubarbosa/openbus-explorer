package busexplorer.panel;

import javax.swing.JPanel;

/**
 * Extensão do JPanel para permitir um painel receber notificações de 'refresh'
 * e então notificar seus constituintes para preparar os recursos externos necessários.
 *
 * @author Tecgraf
 */
public abstract class RefreshablePanel extends JPanel implements RefreshDelegate {
}
