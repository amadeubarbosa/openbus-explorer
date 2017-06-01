package busexplorer.panel;

import busexplorer.utils.Language;

import javax.swing.AbstractAction;
import java.awt.Window;

/**
 * Ação que contém os principais componentes a serem utilizados na janela
 * principal da aplicação com suporte embutido à internacionalização através
 * do uso de {@link Language}.
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado ao {@link TablePanelComponent} relacionado a
 *        esta ação.
 */
public abstract class OpenBusAction<T> extends AbstractAction implements
        TablePanelActionInterface<T> {

  protected Window parentWindow;
  private TablePanelComponent<T> panel;

  /**
   * Construtor básico para ação que definirá o nome da ação a partir
   * da busca usando {@link Language#get(Class, String)} com a classe concreta e a chave {@code .name}.
   *
   * @param parentWindow janela
   */
  public OpenBusAction(Window parentWindow) {
    this.putValue(NAME, getString("name"));
    this.parentWindow = parentWindow;
  }

  /**
   * Construtor para permitir a personalização do nome da ação.
   *  @param parentWindow janela
   * @param actionName nome da ação
   */
  public OpenBusAction(Window parentWindow, String actionName) {
    super(actionName);
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
   * Busca pela string com valor associado ao nome da classe concreta
   * e sufixo de uma chave no {@link Language}.
   * 
   * @param key a chave
   * @return a string cujo valor está associado a essa classe concreta e a chave.
   */
  protected String getString(String key) {
    return Language.get(this.getClass(), key);
  }
}
