package busexplorer.desktop.dialog;

import reuse.modified.logistic.client.util.InputDialog;
import tecgraf.openbus.admin.BusAdmin;

import java.awt.Window;

/**
 * A classe abstrata BusExplorerAbstractInputDialog implementa as
 * funcionalidades básicas dos diálogos de entrada do BusExplorer.
 * 
 * @author Tecgraf
 */
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
