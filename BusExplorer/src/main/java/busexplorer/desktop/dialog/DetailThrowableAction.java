package busexplorer.desktop.dialog;

import busexplorer.ApplicationIcons;
import busexplorer.utils.Language;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import java.awt.event.ActionEvent;

/**
 * Ação que ao ser requisitada abre um diálogo detalhando o erro/exceção
 * apresentado pela janela corrente. Em seguida fecha a janela corrente.
 */
final class DetailThrowableAction extends AbstractAction {

  private final JDialog owner;
  private Throwable throwable;

  protected DetailThrowableAction(JDialog owner, Throwable throwable) {
    putValue(Action.NAME, getString("name"));
    putValue(Action.MNEMONIC_KEY, (int) getString("mnemonic").charAt(0));
    putValue(Action.SMALL_ICON, ApplicationIcons.ICON_DEBUG_16);
    putValue(Action.SHORT_DESCRIPTION, getString("tooltip"));
    this.owner = owner;
    this.throwable = throwable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (owner != null) {
      owner.dispose();
    }
    ExceptionDialog dialog =
      ExceptionDialog.createDialog(this.owner, this.owner.getTitle(), this.throwable);
    dialog.setVisible(true);
  }

  protected String getString(String key) {
    return Language.get(this.getClass(), key);
  }
}
