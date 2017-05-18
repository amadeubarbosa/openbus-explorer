package busexplorer.desktop.dialog;

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
   *  Refer�ncia para fachada {@link BusAdmin} do Servi�o de Configura��o.
   */
  protected BusAdmin admin;

  /**
   * Construtor b�sico para preencher a refer�ncia do {@link BusAdmin}.
   * 
   * @param parentWindow Janela m�e do Di�logo
   * @param admin Refer�ncia para fachada {@link BusAdmin} do Servi�o de Configura��o.
   */
  public BusExplorerAbstractInputDialog(Window parentWindow, BusAdmin admin) {
    super(parentWindow);
    this.admin = admin;
  }

  /**
   * Construtor para preencher a refer�ncia do {@link BusAdmin} e um t�tulo pr�-definido.
   *
   * @param parentWindow Janela m�e do Di�logo
   * @param title T�tulo do Di�logo.
   * @param admin Refer�ncia para fachada {@link BusAdmin} do Servi�o de Configura��o.
   */
  public BusExplorerAbstractInputDialog(Window parentWindow, String title, BusAdmin admin) {
    super(parentWindow, title);
    this.admin = admin;
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
