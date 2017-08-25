package busexplorer.desktop.dialog;

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
  private String[] additionalInfo;

  protected DetailThrowableAction(JDialog owner, Throwable throwable, String[] additionalInfo) {
    putValue(Action.NAME, getString("name"));
    putValue(Action.MNEMONIC_KEY, (int) getString("mnemonic").charAt(0));
    this.owner = owner;
    this.throwable = throwable;
    this.additionalInfo = additionalInfo;
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
      ExceptionDialog.createDialog(this.owner, this.owner.getTitle(), this.throwable,
        this.additionalInfo);
    dialog.setVisible(true);
  }

  protected String getString(String key) {
    return Language.get(this.getClass(), key);
  }
}
