package busexplorer.desktop.dialog;

import tecgraf.openbus.admin.BusAdminFacade;

import java.awt.Window;

/**
 * A classe abstrata BusExplorerAbstractInputDialog implementa as
 * funcionalidades básicas dos diálogos de entrada do BusExplorer.
 * 
 * @author Tecgraf
 */
public abstract class BusExplorerAbstractInputDialog extends InputDialog {

  /**
   *  Referência para fachada {@link BusAdminFacade} do Serviço de Configuração.
   */
  protected BusAdminFacade admin;

  /**
   * Construtor básico para preencher a referência do {@link BusAdminFacade}.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param admin Referência para fachada {@link BusAdminFacade} do Serviço de Configuração.
   */
  public BusExplorerAbstractInputDialog(Window parentWindow, BusAdminFacade admin) {
    super(parentWindow);
    this.admin = admin;
  }

  /**
   * Construtor para preencher a referência do {@link BusAdminFacade} e um título pré-definido.
   *
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   * @param admin Referência para fachada {@link BusAdminFacade} do Serviço de Configuração.
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
