package busexplorer.desktop.dialog;

import reuse.modified.logistic.client.util.InputDialog;
import tecgraf.openbus.admin.BusAdmin;

import java.awt.Window;

/**
 * A classe abstrata BusExplorerAbstractInputDialog implementa as
 * funcionalidades b�sicas dos di�logos de entrada do BusExplorer.
 * 
 * @author Tecgraf
 */
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
