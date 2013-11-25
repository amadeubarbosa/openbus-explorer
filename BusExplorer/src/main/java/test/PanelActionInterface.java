package test;

import javax.swing.Action;

/**
 * Interface que define ações associadas ao componente {@link PanelComponent}.
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado ao {@link PanelComponent} relacionado a
 *        esta ação.
 */
public interface PanelActionInterface<T> extends Action {

  /**
   * Retorna qual o tipo da ação que será usada no CRUD
   * 
   * @return ADD para ações de adição; EDIT para ações de edição; REMOVE para
   *         ações de remoção; OTHER para outros tipos de ações que não as 3
   *         acima;
   */
  public ActionType getActionType();

  /**
   * Recupera o componente {@link PanelComponent}
   * 
   * @return o componente associado a ação.
   */
  public PanelComponent<T> getPanelComponent();

  /**
   * Recupera o painel associado à ação.
   * 
   * @param panel o componente.
   */
  public void setPanelComponent(PanelComponent<T> panel);
}
