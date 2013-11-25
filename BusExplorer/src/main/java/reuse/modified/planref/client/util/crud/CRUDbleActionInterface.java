package reuse.modified.planref.client.util.crud;

import javax.swing.Action;

/**
 * Interface que define os tipos de actions dos crud panels
 * 
 * @author Tecgraf
 */
public interface CRUDbleActionInterface extends Action {

  /**
   * Retorna qual o tipo da a��o que ser� usada no CRUD
   * 
   * @return ADD para a��es de adi��o; EDIT para a��es de edi��o; REMOVE para
   *         a��es de remo��o; OTHER para outros tipos de a��es que n�o as 3
   *         acima;
   */
  public CRUDActionType crudActionType();

  /** Constantes que definem os tipos de bot�es que porder�o ser utilizados */
  public enum CRUDActionType {
    /** Adi��o */
    ADD,
    /** Edi��o */
    EDIT,
    /** Remo��o */
    REMOVE,
    /** Atualizar */
    REFRESH,
    /** Outros tipos de a��es */
    OTHER,
    /** A��o generica que requer um item selecionado */
    OTHER_SINGLE_SELECTION,
    /** A��o generica que requer pelo menos um item selecionado */
    OTHER_MULTI_SELECTION,
    /** A��o generica que requer multiplos itens selecionados */
    OTHER_ONLY_MULTI_SELECTION;
  }
}
