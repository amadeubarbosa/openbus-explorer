package busexplorer.panel;

import busexplorer.utils.Language;

import javax.swing.AbstractAction;
import java.awt.Window;

/**
 * A��o que cont�m os principais componentes a serem utilizados na janela
 * principal da aplica��o com suporte embutido � internacionaliza��o atrav�s
 * do uso de {@link Language}.
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado ao {@link TablePanelComponent} relacionado a
 *        esta a��o.
 */
public abstract class OpenBusAction<T> extends AbstractAction implements
        TablePanelActionInterface<T> {

  protected Window parentWindow;
  private TablePanelComponent<T> panel;

  /**
   * Construtor b�sico para a��o que definir� o nome da a��o a partir
   * da busca usando {@link Language#get(Class, String)} com a classe concreta e a chave {@code .name}.
   *
   * @param parentWindow janela
   */
  public OpenBusAction(Window parentWindow) {
    this.putValue(NAME, getString("name"));
    this.parentWindow = parentWindow;
  }

  /**
   * Construtor para permitir a personaliza��o do nome da a��o.
   *  @param parentWindow janela
   * @param actionName nome da a��o
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
   * @return a string cujo valor est� associado a essa classe concreta e a chave.
   */
  protected String getString(String key) {
    return Language.get(this.getClass(), key);
  }
}
