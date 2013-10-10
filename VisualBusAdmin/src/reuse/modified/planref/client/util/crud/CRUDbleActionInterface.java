package reuse.modified.planref.client.util.crud;

import javax.swing.Action;

/**
 * Interface que define os tipos de actions dos crud panels
 * 
 * @author Tecgraf
 */
public interface CRUDbleActionInterface extends Action {
  /** Constantes que definem os tipos de botões que porderão ser utilizados */
  /** Adição */
  public static final int TYPE_ACTION_ADD = 0;
  /** Edição */
  public static final int TYPE_ACTION_EDIT = 1;
  /** Remoção */
  public static final int TYPE_ACTION_REMOVE = 2;
  /** Outros tipos de ações */
  public static final int TYPE_ACTION_OTHER = 3;
  /** Ação generica que requer um item selecionado */
  public static final int TYPE_ACTION_OTHER_SINGLE_SELECTION = 4;
  /** Ação generica que requer pelo menos um item selecionado */
  public static final int TYPE_ACTION_OTHER_MULTI_SELECTION = 5;
  /** Ação generica que requer multiplos itens selecionados */
  public static final int TYPE_ACTION_OTHER_ONLY_MULTI_SELECTION = 6;

  /**
   * Retorna qual o tipo da ação que será usada no CRUD
   * 
   * @return TYPE_ACTION_ADD para ações de adição; TYPE_ACTION_EDIT para ações
   *         de edição; TYPE_ACTION_REMOVE para ações de remoção;
   *         TYPE_ACTION_OTHER para outros tipos de ações que não as 3 acima;
   */
  public abstract int crudActionType();
}
