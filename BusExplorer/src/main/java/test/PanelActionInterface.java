package test;

import javax.swing.Action;

/**
 * Interface que define a��es associadas ao componente {@link PanelComponent}.
 * 
 * @author Tecgraf
 * @param <T> Tipo de dado associado ao {@link PanelComponent} relacionado a
 *        esta a��o.
 */
public interface PanelActionInterface<T> extends Action {

  /**
   * Retorna qual o tipo da a��o que ser� usada no CRUD
   * 
   * @return ADD para a��es de adi��o; EDIT para a��es de edi��o; REMOVE para
   *         a��es de remo��o; OTHER para outros tipos de a��es que n�o as 3
   *         acima;
   */
  public ActionType getActionType();

  /**
   * Recupera o componente {@link PanelComponent}
   * 
   * @return o componente associado a a��o.
   */
  public PanelComponent<T> getPanelComponent();

  /**
   * Recupera o painel associado � a��o.
   * 
   * @param panel o componente.
   */
  public void setPanelComponent(PanelComponent<T> panel);
}
