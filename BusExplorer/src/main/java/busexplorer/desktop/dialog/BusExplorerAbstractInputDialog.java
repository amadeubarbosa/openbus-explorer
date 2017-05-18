package busexplorer.desktop.dialog;

import tecgraf.openbus.admin.BusAdminFacade;

import java.awt.Window;

/**
 * A classe abstrata BusExplorerAbstractInputDialog implementa as
 * funcionalidades b�sicas dos di�logos de entrada do BusExplorer.
 * 
 * @author Tecgraf
 */
public abstract class BusExplorerAbstractInputDialog extends InputDialog {

  /**
   *  Refer�ncia para fachada {@link BusAdminFacade} do Servi�o de Configura��o.
   */
  protected BusAdminFacade admin;

  /**
   * Construtor b�sico para preencher a refer�ncia do {@link BusAdminFacade}.
   * 
   * @param parentWindow Janela m�e do Di�logo
   * @param admin Refer�ncia para fachada {@link BusAdminFacade} do Servi�o de Configura��o.
   */
  public BusExplorerAbstractInputDialog(Window parentWindow, BusAdminFacade admin) {
    super(parentWindow);
    this.admin = admin;
  }

  /**
   * Construtor para preencher a refer�ncia do {@link BusAdminFacade} e um t�tulo pr�-definido.
   *
   * @param parentWindow Janela m�e do Di�logo
   * @param title T�tulo do Di�logo.
   * @param admin Refer�ncia para fachada {@link BusAdminFacade} do Servi�o de Configura��o.
   */
  public BusExplorerAbstractInputDialog(Window parentWindow, String title, BusAdminFacade admin) {
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
