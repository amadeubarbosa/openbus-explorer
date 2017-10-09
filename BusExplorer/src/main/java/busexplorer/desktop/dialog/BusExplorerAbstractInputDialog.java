package busexplorer.desktop.dialog;

import java.awt.Window;

/**
 * A classe abstrata BusExplorerAbstractInputDialog implementa as
 * funcionalidades básicas dos diálogos de entrada do BusExplorer.
 * 
 * @author Tecgraf
 */
public abstract class BusExplorerAbstractInputDialog extends InputDialog {

  /**
   * {@inheritDoc}
   */
  public BusExplorerAbstractInputDialog(Window parentWindow) {
    super(parentWindow);
  }

  /**
   * {@inheritDoc}
   */
  public BusExplorerAbstractInputDialog(Window parentWindow, String title) {
    super(parentWindow, title);
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
