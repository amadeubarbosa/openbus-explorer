package busexplorer.exception;

import java.awt.Window;

import reuse.modified.logistic.client.util.InputDialog;
import admin.BusAdmin;

public abstract class BusExplorerAbstractInputDialog extends InputDialog {

  /**
   * Construtor.
   * 
   * @param parentWindow Janela m�e do Di�logo
   * @param title T�tulo do Di�logo.
   * @param admin Biblioteca de administra��o
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
