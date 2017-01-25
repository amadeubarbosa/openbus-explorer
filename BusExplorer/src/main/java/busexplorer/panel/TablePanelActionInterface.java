package busexplorer.panel;

import javax.swing.Action;

/**
 * Interface que define ações associadas ao componente {@link TablePanelComponent}.
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado ao {@link TablePanelComponent} relacionado a
 *        esta ação.
 */
public interface TablePanelActionInterface<T> extends Action {

  /**
   * Retorna qual o tipo da ação que será usada no CRUD
   * 
   * @return ADD para ações de adição; EDIT para ações de edição; REMOVE para
   *         ações de remoção; OTHER para outros tipos de ações que não as 3
   *         acima;
   */
  ActionType getActionType();

  /**
   * Recupera o componente {@link TablePanelComponent}
   * 
   * @return o componente associado a ação.
   */
  public TablePanelComponent<T> getTablePanelComponent();

  /**
   * Recupera o painel associado à ação.
   * 
   * @param panel o componente.
   */
  public void setTablePanelComponent(TablePanelComponent<T> panel);

  /**
   * Indica condições de habilitação da ação.
   *
   * @return condições de habilitação da ação. 
   */
  boolean abilityConditions();
}
