package busexplorer.desktop.dialog;

import javax.swing.JDialog;

/**
 * Ação que ao ser requisitada fecha a janela corrente.
 */
public class CloseAction extends CancelAction {
  public CloseAction(JDialog owner) {
    super(owner);
  }
}
