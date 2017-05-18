package busexplorer.desktop.dialog;

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
   *  Referência para fachada {@link BusAdmin} do Serviço de Configuração.
   */
  protected BusAdmin admin;

  /**
   * Construtor básico para preencher a referência do {@link BusAdmin}.
   * 
   * @param parentWindow Janela mãe do Diálogo
   * @param admin Referência para fachada {@link BusAdmin} do Serviço de Configuração.
   */
  public BusExplorerAbstractInputDialog(Window parentWindow, BusAdmin admin) {
    super(parentWindow);
    this.admin = admin;
  }

  /**
   * Construtor para preencher a referência do {@link BusAdmin} e um título pré-definido.
   *
   * @param parentWindow Janela mãe do Diálogo
   * @param title Título do Diálogo.
   * @param admin Referência para fachada {@link BusAdmin} do Serviço de Configuração.
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
