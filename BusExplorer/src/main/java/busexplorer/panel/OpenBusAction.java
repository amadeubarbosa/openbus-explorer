package busexplorer.panel;

import busexplorer.utils.Utils;
import tecgraf.openbus.admin.BusAdmin;

import javax.swing.AbstractAction;
import java.awt.Window;

/**
 * A��o que cont�m os principais componentes a serem utilizados na janela
 * principal da aplica��o
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado ao {@link TablePanelComponent} relacionado a
 *        esta a��o.
 */
public abstract class OpenBusAction<T> extends AbstractAction implements
        TablePanelActionInterface<T> {

  protected BusAdmin admin;
  protected Window parentWindow;
  private TablePanelComponent<T> panel;

  /**
   * Construtor.
   * 
   * @param parentWindow
   * @param admin
   * @param actionName nome da a��o
   */
  public OpenBusAction(Window parentWindow, BusAdmin admin, String actionName) {
    super(actionName);
    this.admin = admin;
    this.parentWindow = parentWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTablePanelComponent(TablePanelComponent<T> panel) {
    this.panel = panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TablePanelComponent<T> getTablePanelComponent() {
    return this.panel;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean abilityConditions() {
    return true;
  }

  /**
   * Busca pelo valor associado a chave no LNG
   * 
   * @param key a chave
   * @return o valor associado � chave.
   */
  protected String getString(String key) {
    return Utils.getString(this.getClass(), key);
  }
}
