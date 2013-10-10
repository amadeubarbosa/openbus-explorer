package reuse.modified.planref.client.util.crud;

import java.util.ArrayList;
import java.util.List;

import reuse.modified.logistic.logic.common.Code;
import reuse.modified.logistic.logic.common.Identifiable;

/**
 * Classe que surgiu para encapsular o retorno dos itens de uma Funcionalidade
 * de CRUD editáveis. Esta classe é composta por duas listas, uma de itens
 * adicionados e editados e a outra para itens removidos.
 * 
 * @param <T> Tipo do Dado do CRUDPanel.
 * 
 * @see CRUDPanel
 * @see CRUDMonitoredFrame
 * @see ModifiableObjectTableModel
 * 
 * @author Tecgraf
 */
public class CRUDAlteredItems<T extends Identifiable<T>> {

  /** Lista de itens adicionados ou editados do CRUD. */
  private final List<T> addedAndEditedItems;
  /** Lista de itens removidos do CRUD */
  private final List<T> removedItems;

  /**
   * Construtor.
   * 
   * @param addedAndEditedItems Lista de itens editados e adicionados.
   * @param removedItems Lista de itens removidos.
   */
  public CRUDAlteredItems(List<T> addedAndEditedItems, List<T> removedItems) {
    this.addedAndEditedItems = addedAndEditedItems;
    this.removedItems = removedItems;
  }

  /**
   * Método que retorna a lista de itens adicionados e editados.
   * 
   * @return Lista de itens adicionados e editados.
   */
  public List<T> getAddedAndEditedItems() {
    return addedAndEditedItems;
  }

  /**
   * Método que retorna os identificadores dos itens removidos.
   * 
   * @return Lista de itens removidos.
   */
  public List<Code<T>> getRemovedIds() {
    List<Code<T>> removedIds = new ArrayList<Code<T>>();
    for (T item : removedItems) {
      removedIds.add(item.getId());
    }
    return removedIds;
  }

  /**
   * Método que retorna os itens removidos.
   * 
   * @return Itens removidos.
   */
  public List<T> getRemovedItems() {
    return removedItems;
  }

  /**
   * Verifica se a lista de alterações da tabela esta vazia.
   * 
   * @return <code>true</code> se a lista de alterações for vazia, e
   *         <code>false</code> caso contrário.
   */
  public boolean isEmpty() {
    return addedAndEditedItems.isEmpty() && removedItems.isEmpty();
  }

}
