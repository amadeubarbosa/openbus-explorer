package reuse.modified.planref.client.util.crud;

import javax.swing.Action;

/**
 * Interface que define os tipos de actions dos crud panels
 * 
 * @author Tecgraf
 */
public interface CRUDbleActionInterface extends Action {

  /**
   * Retorna qual o tipo da ação que será usada no CRUD
   * 
   * @return ADD para ações de adição; EDIT para ações de edição; REMOVE para
   *         ações de remoção; OTHER para outros tipos de ações que não as 3
   *         acima;
   */
  public CRUDActionType crudActionType();

  /** Constantes que definem os tipos de botões que porderão ser utilizados */
  public enum CRUDActionType {
    /** Adição */
    ADD,
    /** Edição */
    EDIT,
    /** Remoção */
    REMOVE,
    /** Atualizar */
    REFRESH,
    /** Outros tipos de ações */
    OTHER,
    /** Ação generica que requer um item selecionado */
    OTHER_SINGLE_SELECTION,
    /** Ação generica que requer pelo menos um item selecionado */
    OTHER_MULTI_SELECTION,
    /** Ação generica que requer multiplos itens selecionados */
    OTHER_ONLY_MULTI_SELECTION;
  }
}
