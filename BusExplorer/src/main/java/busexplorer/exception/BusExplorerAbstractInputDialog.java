package busexplorer.exception;

import javax.swing.JFrame;

import reuse.modified.logistic.client.util.InputDialog;
import tecgraf.javautils.LNG;
import admin.BusAdmin;

public abstract class BusExplorerAbstractInputDialog extends InputDialog {

  /**
   * Construtor.
   * 
   * @param parentWindow Janela m�e do Di�logo
   * @param title T�tulo do Di�logo.
   * @param admin Biblioteca de administra��o
   */
  public BusExplorerAbstractInputDialog(JFrame parentWindow, String title,
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

  /**
   * Busca pelo valor associado a chave no LNG
   * 
   * @param key a chave
   * @return o valor associado � chave.
   */
  protected String getString(String key) {
    return LNG.get(this.getClass().getSimpleName() + "." + key);
  }
}
