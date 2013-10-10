package reuse.modified.planref.client.util.crud;

import javax.swing.Action;

/**
 * Interface que define os tipos de actions dos crud panels
 * 
 * @author Tecgraf
 */
public interface CRUDbleActionInterface extends Action {
  /** Constantes que definem os tipos de bot�es que porder�o ser utilizados */
  /** Adi��o */
  public static final int TYPE_ACTION_ADD = 0;
  /** Edi��o */
  public static final int TYPE_ACTION_EDIT = 1;
  /** Remo��o */
  public static final int TYPE_ACTION_REMOVE = 2;
  /** Outros tipos de a��es */
  public static final int TYPE_ACTION_OTHER = 3;
  /** A��o generica que requer um item selecionado */
  public static final int TYPE_ACTION_OTHER_SINGLE_SELECTION = 4;
  /** A��o generica que requer pelo menos um item selecionado */
  public static final int TYPE_ACTION_OTHER_MULTI_SELECTION = 5;
  /** A��o generica que requer multiplos itens selecionados */
  public static final int TYPE_ACTION_OTHER_ONLY_MULTI_SELECTION = 6;

  /**
   * Retorna qual o tipo da a��o que ser� usada no CRUD
   * 
   * @return TYPE_ACTION_ADD para a��es de adi��o; TYPE_ACTION_EDIT para a��es
   *         de edi��o; TYPE_ACTION_REMOVE para a��es de remo��o;
   *         TYPE_ACTION_OTHER para outros tipos de a��es que n�o as 3 acima;
   */
  public abstract int crudActionType();
}
