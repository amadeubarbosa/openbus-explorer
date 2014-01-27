package busexplorer.exception;

import java.awt.Window;

import reuse.modified.logistic.client.util.InputDialog;
import admin.BusAdmin;

public abstract class BusExplorerAbstractInputDialog extends InputDialog {

  /**
   * Construtor.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   * @param admin Biblioteca de administração
   */
  public BusExplorerAbstractInputDialog(Window parentWindow, String title,
    BusAdmin admin) {
    super(parentWindow, title, admin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void showDialog() {
    // Emulando tratamento de modalidade
    getOwner().setEnabled(false);
    super.showDialog();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose() {
    // Emulando tratamento de modalidade
    getOwner().setEnabled(true);
    super.dispose();
  }

}
