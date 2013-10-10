package reuse.modified.planref.client.util.crud;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import reuse.modified.logistic.logic.common.Code;
import reuse.modified.logistic.logic.common.Identifiable;
import tecgraf.javautils.gui.table.ObjectTableModel;
import tecgraf.javautils.gui.table.ObjectTableProvider;

/**
 * Modelo de exibição de dados editáveis para tabelas.
 * 
 * @author Tecgraf
 * @param <T> Tipo do dado que será exibido.
 */
public class ModifiableObjectTableModel<T extends Identifiable<T>> extends
  ObjectTableModel<T> {

  /** Índice da primeira linha do modelo. */
  private static final int FIRST_ROW_INDEX = 0;
  /**
   * Mapa que guarda os estados dos objetos exibidos que sofreram qualquer tirpo
   * de alteração.
   */
  private HashMap<T, State> stateMap = new HashMap<T, State>();

  /**
   * Tipos de alterações sofridas pelos itens.
   * 
   * @author Tecgraf
   */
  private enum State {
    /** Adicionado */
    ADDED,
    /** Editado */
    EDITED,
    /** Removido */
    REMOVED,
    /** Inválido após edição */
    INVALID
  };

  /**
   * Construtor
   * 
   * @param rows Coleção de objetos que será exibida na tabela.
   * @param provider Componente responsável por fazer relação entre as operações
   *        do modelo e a lógica do objeto exibido.
   */
  public ModifiableObjectTableModel(List<T> rows,
    ObjectTableProvider<T> provider) {
    super(rows, provider);
  }

  /**
   * Método que atualiza o estado de um item.
   * 
   * @param item Item que terá seu estado alterado
   * @param state Novo estado do objeto.
   */
  private void setState(T item, State state) {
    //Remove antes de adicionar, pois caso exista no mapa o item, o hashmap não atualiza a chave, apenas o valor.
    if (stateMap.containsKey(item)) {
      stateMap.remove(item);
    }
    stateMap.put(item, state);
  }

  /**
   * Método que altera o estado de um objeto para INSERIDO no mapa de estados.
   * 
   * @param item Item que terá seu estado alterado.
   */
  public void itemAdded(T item) {
    setState(item, State.ADDED);
  }

  /**
   * Método que altera o estado de um objeto para EDITADO no mapa de estados.
   * 
   * @param item Item que terá seu estado alterado.
   */
  public void itemEdited(T item) {
    setState(item, State.EDITED);
  }

  /**
   * Método que altera o estado de um objeto para REMOVIDO no mapa de estados.
   * 
   * @param item Item que terá seu estado alterado.
   */
  public void itemRemoved(T item) {
    setState(item, State.REMOVED);
  }

  /**
   * Método que altera o estado de um objeto para INVÁLIDO no mapa de estados.
   * 
   * @param item Item que terá seu estado alterado.
   */
  public void itemInvalid(T item) {
    setState(item, State.INVALID);
  }

  /**
   * Método que responde o estado de um item do mapa.
   * 
   * @param item que se deseja saber o estado.
   * @return Estado do item.
   * 
   */
  public State getState(T item) {
    return stateMap.get(item);
  }

  /**
   * Método que retira do mapa de estados um item.
   * 
   * @param item que não terá seu estado controlado.
   * @return Estado do item removido.
   */
  public State removeItemState(T item) {
    return stateMap.remove(item);
  }

  /**
   * Método que retira do mapa de estados um item.
   * 
   * @param itemId item que não terá seu estado controlado.
   * @return Último estado do item, ou <code>null</code> caso o item não exista
   *         no mapa.
   */
  public State removeItemState(Code<T> itemId) {
    for (T item : stateMap.keySet()) {
      if (item.getId().equals(itemId)) {
        return stateMap.remove(item);
      }
    }
    return null;
  }

  /**
   * Método que verifica se um item tem o estado de novo.
   * 
   * @param row Índice da linha do item.
   * @param col Índice da coluna do item.
   * @return Verdadeiro ou Falso.
   */
  public boolean isValueAtNew(int row, int col) {
    State state = getValueState(row, col);
    if ((state != null) && (state.equals(State.ADDED))) {
      return true;
    }
    return false;
  }

  /**
   * Método que verifica se um item tem o estado de editado.
   * 
   * @param row Índice da linha do item.
   * @param col Índice da coluna do item.
   * @return Verdadeiro ou Falso.
   */
  public boolean wasValueAtEdited(int row, int col) {
    State state = getValueState(row, col);
    if ((state != null)
      && (state.equals(State.EDITED) || state.equals(State.INVALID))) {
      return true;
    }
    return false;
  }

  /**
   * Método que verifica se um item tem o estado de inválido.
   * 
   * @param row Índice da linha do item.
   * @param col Índice da coluna do item.
   * @return Verdadeiro ou Falso.
   */
  public boolean isValueAtInvalid(int row, int col) {
    if (getProvider() instanceof VerifiableModifiableObjectTableProvider) {
      VerifiableModifiableObjectTableProvider verifiableProvider =
        (VerifiableModifiableObjectTableProvider) getProvider();
      T value = getRows().get(row);
      return !verifiableProvider.isValid(value, col);
    }
    return false;
  }

  /**
   * Método que verifica se existem linhas inválidas
   * 
   * @return Verdadeiro se existem linhas inválidas, falso caso contrário.
   */
  public boolean hasInvalidRows() {
    for (State state : this.stateMap.values()) {
      if (state.equals(State.INVALID)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Método que retorna todos os itens do mapa separados por estados através do
   * objeto encapsulador CRUDAlteredItems.
   * 
   * @see CRUDAlteredItems
   * @return Itens separados por estado.
   */
  public CRUDAlteredItems<T> getItensByState() {
    ArrayList<T> addedAndEditedItems = new ArrayList<T>();
    ArrayList<T> removedItems = new ArrayList<T>();
    for (T item : stateMap.keySet()) {
      State state = stateMap.get(item);
      if (state.equals(State.ADDED) || state.equals(State.EDITED)) {
        addedAndEditedItems.add(item);
      }
      else if (state.equals(State.REMOVED)) {
        removedItems.add(item);
      }
    }
    return new CRUDAlteredItems<T>(addedAndEditedItems, removedItems);
  }

  /**
   * @return Itens adicionados.
   */
  public List<T> getAddedItems() {
    List<T> addedItems = new ArrayList<T>();
    for (T item : stateMap.keySet()) {
      State state = stateMap.get(item);
      if (state.equals(State.ADDED)) {
        addedItems.add(item);
      }
    }
    return addedItems;
  }

  /**
   * @return Itens editados.
   */
  public List<T> getEditedItems() {
    List<T> editedItems = new ArrayList<T>();
    for (T item : stateMap.keySet()) {
      State state = stateMap.get(item);
      if (state.equals(State.EDITED)) {
        editedItems.add(item);
      }
    }
    return editedItems;
  }

  /**
   * Método que retorna o estado de um item do CRUD.
   * 
   * @param row Índice da linha
   * @param col Índice da coluna, que atualmente não é usado. Devido a definição
   *        de CRUD (cada linha é um objeto) se algum valor do objeto é alterado
   *        então seu estado (como um todo) é editado. No entanto fica pronta a
   *        estrutura para o caso desta regra mudar.
   * @return Estado do objeto.
   */
  private State getValueState(int row, int col) {
    T value = getRows().get(row);
    return stateMap.get(value);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueAt(Object newValue, int rowIndex, int colIndex) {
    boolean modify = false;

    Object oldValue = getValueAt(rowIndex, colIndex);
    if (oldValue == null) {
      if (newValue == null) {
        // do nothing
      }
      else {
        modify = true;
      }
    }
    else {
      if (newValue == null) {
        modify = true;
      }
      else {
        if (newValue.getClass().equals(oldValue.getClass())) {
          if (newValue.equals(oldValue)) {
            // do nothing
          }
          else {
            modify = true;
          }
        }
        else {
          throw new IllegalArgumentException(
            "Classes diferentes durante a modificação do objeto. Classe do novo valor: "
              + newValue.getClass() + ". Classe do valor antigo: "
              + oldValue.getClass() + ". Coluna: " + colIndex);
        }
      }
    }

    if (modify == true) {
      T row = getRows().get(rowIndex);
      ObjectTableProvider<T> provider = getProvider();
      if (provider instanceof VerifiableModifiableObjectTableProvider) {
        VerifiableModifiableObjectTableProvider verifiableProvider =
          (VerifiableModifiableObjectTableProvider) provider;
        verifiableProvider.setValueAt(row, newValue, colIndex);
        itemEdited(row);
        if (!isRowValid(row)) {
          itemInvalid(row);
        }
        fireTableRowsUpdated(FIRST_ROW_INDEX, getLastRowIndex());
      }
      else {
        super.setValueAt(newValue, rowIndex, colIndex);
        itemEdited(row);
      }
    }
    else {
      // do nothing
    }
  }

  /**
   * @return O índice da última linha do modelo.
   */
  private int getLastRowIndex() {
    return getRowCount() - 1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add(T row) {
    if (row == null) {
      throw new IllegalArgumentException("row == null");
    }
    List<T> rows = getRows();
    int index = rows.indexOf(row);
    if (index != -1) {
      rows.set(index, row);
      itemEdited(row);
      fireTableRowsUpdated(index, index);
    }
    else {
      rows.add(row);
      itemAdded(row);
      index = rows.indexOf(row);
      fireTableRowsInserted(index, index);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addAll(Collection<T> rows) {
    for (T item : rows) {
      this.add(item);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean remove(T row) {
    if (super.remove(row)) {
      itemRemoved(row);
      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T remove(int rowIndex) {
    T row = super.remove(rowIndex);
    itemRemoved(row);
    return row;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<T> removeAll(int[] rowIndexes) {
    for (int i : rowIndexes) {
      itemRemoved(getRow(i));
    }
    Collection<T> allRemoved = super.removeAll(rowIndexes);
    return allRemoved;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Collection<T> removeAll() {
    Collection<T> allRows = super.removeAll();
    for (T item : allRows) {
      // TODO - PLANREF: Ver regra de remoção, coloca no mapa ou limpa o mapa!!!
      itemRemoved(item);
    }
    return allRows;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean modify(T row) {
    if (super.modify(row)) {
      itemEdited(row);
      return true;
    }
    return false;
  }

  /**
   * Método que atualiza o CRUD com alterações recebidas (Ex: de um retorno de
   * serviço)
   * 
   * @param addedAndEditedItems Lista de itens alterados e adicionados.
   * @param removedIds Lista de identificadores itens removidos.
   */
  public void updateData(List<T> addedAndEditedItems, List<Code<T>> removedIds) {
    // a ordem das chamadas é importante e queremos remover antes de atualizar.
    removeWithOutStates(removedIds);
    addWithOutStates(addedAndEditedItems);
  }

  /**
   * Método que adiciona items na lista do modelo sem alterar o mapa de estados
   * do modelo.
   * 
   * @param addedAndEditedItems Lista de itens a serem inseridos.
   */
  private void addWithOutStates(List<T> addedAndEditedItems) {
    super.addAll(addedAndEditedItems);
    for (T item : addedAndEditedItems) {
      removeItemState(item);
    }
  }

  /**
   * Método que remove itens da lista do modelo sem alterar o mapa de estados do
   * modelo.
   * 
   * @param removedIds Lista de identificadores de objetos a serem removidos.
   */
  private void removeWithOutStates(List<Code<T>> removedIds) {
    HashMap<Code<T>, T> objectMap = new HashMap<Code<T>, T>();
    List<T> rows = getRows();
    for (T row : rows) {
      objectMap.put(row.getId(), row);
    }
    for (Code<T> itemId : removedIds) {
      T item = objectMap.get(itemId);
      if (item != null) {
        super.remove(item);
      }
    }

  }

  /**
   * Método que limpa os estados de todos os objetos.
   */
  public void clearStates() {
    stateMap = new HashMap<T, State>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    ObjectTableProvider<T> provider = getProvider();
    if (provider instanceof VerifiableModifiableObjectTableProvider) {
      VerifiableModifiableObjectTableProvider verifiableProvider =
        (VerifiableModifiableObjectTableProvider) provider;
      T row = getRow(rowIndex);
      return verifiableProvider.isCellEditable(row, rowIndex, columnIndex);
    }
    else {
      return super.isCellEditable(rowIndex, columnIndex);
    }
  }

  /**
   * Verifica se a linha está válida.
   * 
   * @param row a linha.
   * @return <code>true</code> se válida, e <code>false</code> caso contrário.
   */
  private boolean isRowValid(T row) {
    ObjectTableProvider<T> provider = getProvider();
    if (provider instanceof VerifiableModifiableObjectTableProvider) {
      VerifiableModifiableObjectTableProvider verifiableProvider =
        (VerifiableModifiableObjectTableProvider) provider;
      // verify if the row is valid
      for (int i = 0; i < getColumnCount(); i++) {
        if (!verifiableProvider.isValid(row, i)) {
          return false;
        }
      }
      return true;
    }
    return true;
  }
}
