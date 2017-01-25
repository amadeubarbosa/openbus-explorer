package busexplorer.panel;

import javax.swing.Action;

/**
 * Interface que define a��es associadas ao componente {@link TablePanelComponent}.
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado ao {@link TablePanelComponent} relacionado a
 *        esta a��o.
 */
public interface TablePanelActionInterface<T> extends Action {

  /**
   * Retorna qual o tipo da a��o que ser� usada no CRUD
   * 
   * @return ADD para a��es de adi��o; EDIT para a��es de edi��o; REMOVE para
   *         a��es de remo��o; OTHER para outros tipos de a��es que n�o as 3
   *         acima;
   */
  ActionType getActionType();

  /**
   * Recupera o componente {@link TablePanelComponent}
   * 
   * @return o componente associado a a��o.
   */
  public TablePanelComponent<T> getTablePanelComponent();

  /**
   * Recupera o painel associado � a��o.
   * 
   * @param panel o componente.
   */
  public void setTablePanelComponent(TablePanelComponent<T> panel);

  /**
   * Indica condi��es de habilita��o da a��o.
   *
   * @return condi��es de habilita��o da a��o. 
   */
  boolean abilityConditions();
}
